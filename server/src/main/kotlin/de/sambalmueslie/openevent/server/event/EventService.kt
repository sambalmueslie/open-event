package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.common.BaseAuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.db.EventData
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EventService(
        private val crudService: EventCrudService,
        private val authenticationHelper: AuthenticationHelper,
        private val entitlementService: ItemEntitlementCrudService,
        private val repository: EventRepository
) : BaseAuthCrudService<Event, EventChangeRequest, EventData>(crudService, authenticationHelper, logger) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
    }

    override fun getAllAccessible(user: User, pageable: Pageable): Page<Event> {
        return repository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
    }

    override fun isAccessAllowed(user: User, obj: EventData): Boolean {
        return getEntitlement(user, obj.id).isGreaterThanEquals(Entitlement.VIEWER)
    }

    override fun isCreationAllowed(user: User, request: EventChangeRequest): Boolean {
        return true
    }

    override fun isModificationAllowed(user: User, obj: EventData): Boolean {
        return getEntitlement(user, obj.id).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
    }

    fun deleteAll(authentication: Authentication, user: User) {
        if (authenticationHelper.isAdmin(authentication)) {
            repository.deleteAll()
        }
    }

    private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemIdAndType(user.id, objId, ItemType.EVENT).entitlement

}
