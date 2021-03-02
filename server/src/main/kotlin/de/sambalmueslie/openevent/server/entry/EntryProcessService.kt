package de.sambalmueslie.openevent.server.entry


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.auth.InsufficientPermissionsException
import de.sambalmueslie.openevent.server.common.BaseAuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.entry.db.EntryProcessData
import de.sambalmueslie.openevent.server.entry.db.EntryProcessRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EntryProcessService(
    private val crudService: EntryProcessCrudService,
    private val authenticationHelper: AuthenticationHelper,
    private val repository: EntryProcessRepository,
    private val entitlementService: ItemEntitlementCrudService,
) : BaseAuthCrudService<EntryProcess, EntryProcessChangeRequest, EntryProcessData>(crudService, authenticationHelper, logger) {


    companion object {
        val logger: Logger = LoggerFactory.getLogger(EntryProcessService::class.java)
    }

    override fun getAllAccessible(user: User, pageable: Pageable): Page<EntryProcess> {
        return repository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
    }

    override fun isAccessAllowed(user: User, obj: EntryProcessData): Boolean {
        return obj.userId == user.id || getEntitlement(user, obj.itemId).isGreaterThanEquals(Entitlement.MANAGER)
    }

    override fun isCreationAllowed(user: User, request: EntryProcessChangeRequest): Boolean {
        return true
    }

    override fun isModificationAllowed(user: User, obj: EntryProcessData): Boolean {
        return obj.userId == user.id || getEntitlement(user, obj.itemId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
    }

    fun deleteAll(authentication: Authentication, user: User) {
        if (authenticationHelper.isAdmin(authentication)) {
            repository.deleteAll()
        }
    }

    fun getUserEntryProcesses(authentication: Authentication, user: User, userId: Long, pageable: Pageable): Page<EntryProcess> {
        return when {
            authenticationHelper.isAdmin(authentication) -> repository.findByUserId(userId, pageable)
            user.id == userId -> repository.findByUserId(userId, pageable)
            else -> throw InsufficientPermissionsException("Cannot access obj due to insufficient permissions")
        }.map { crudService.convert(it) }
    }

    fun getItemEntryProcesses(authentication: Authentication, user: User, itemId: Long, pageable: Pageable): Page<EntryProcess> {
        return when {
            authenticationHelper.isAdmin(authentication) -> repository.findByItemId(itemId, pageable)
            getEntitlement(user, itemId).isGreaterThanEquals(Entitlement.MANAGER) -> repository.findByItemId(itemId, pageable)
            else -> throw InsufficientPermissionsException("Cannot access obj due to insufficient permissions")
        }.map { crudService.convert(it) }
    }

    private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemId(user.id, objId).entitlement

    fun accept(authentication: Authentication, user: User, processId: Long): EntryProcess? {
        return changeStatus(authentication, user, processId, EntryProcessStatus.ACCEPTED)
    }

    fun decline(authentication: Authentication, user: User, processId: Long): EntryProcess? {
        return changeStatus(authentication, user, processId, EntryProcessStatus.DECLINED)
    }

    private fun changeStatus(authentication: Authentication, user: User, processId: Long, status: EntryProcessStatus): EntryProcess? {
        val data = crudService.getData(processId) ?: return null
        if (authenticationHelper.isAdmin(authentication) || getEntitlement(user, data.itemId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)) {
            return crudService.changeStatus(user, data, status)
        } else if (isAccessAllowed(authentication, user, data)) {
            return crudService.convert(data)
        }
        return null
    }


}
