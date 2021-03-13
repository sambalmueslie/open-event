package de.sambalmueslie.openevent.server.structure


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.common.BaseAuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.member.MemberCrudService
import de.sambalmueslie.openevent.server.structure.actions.StructureMemberEntitlementAction
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.structure.db.StructureData
import de.sambalmueslie.openevent.server.structure.db.StructureRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class StructureService(
        private val crudService: StructureCrudService,
        private val authenticationHelper: AuthenticationHelper,
        private val entitlementService: ItemEntitlementCrudService,
        private val repository: StructureRepository
) : BaseAuthCrudService<Structure, StructureChangeRequest, StructureData>(crudService, authenticationHelper, logger) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(StructureService::class.java)
    }


    fun getRoots(authentication: Authentication, user: User, pageable: Pageable): Page<Structure> {
        return if (authenticationHelper.isAdmin(authentication)) {
            repository.findByParentStructureIdIsNull(pageable)
        } else {
            repository.getAllRootAccessible(user.id, pageable)
        }.map { crudService.convert(it) }
    }


    fun getChildren(authentication: Authentication, user: User, objId: Long, pageable: Pageable): Page<Structure> {
        return if (authenticationHelper.isAdmin(authentication)) {
            repository.findByParentStructureId(objId, pageable)
        } else {
            repository.getAllChildAccessible(user.id, objId, pageable)
        }.map { crudService.convert(it) }
    }

    override fun getAllAccessible(user: User, pageable: Pageable): Page<Structure> {
        return repository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
    }

    override fun isAccessAllowed(user: User, obj: StructureData): Boolean {
        return !obj.restricted || getEntitlement(user, obj.id).isGreaterThanEquals(Entitlement.VIEWER)
    }

    override fun isModificationAllowed(user: User, obj: StructureData): Boolean {
        return getEntitlement(user, obj.id).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
    }

    override fun isCreationAllowed(user: User, request: StructureChangeRequest): Boolean {
        val parentId = request.parentStructureId ?: return false
        return getEntitlement(user, parentId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
    }


    fun deleteAll(authentication: Authentication, user: User) {
        if (authenticationHelper.isAdmin(authentication)) {
            repository.deleteAll()
        }
    }

    private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemIdAndType(user.id, objId, ItemType.STRUCTURE).entitlement


}
