package de.sambalmueslie.openevent.server.event.actions


import de.sambalmueslie.openevent.server.common.Action
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.item.api.ItemType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SetupEntitlementAction(private val service: ItemEntitlementCrudService) : Action<Event> {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(SetupEntitlementAction::class.java)
    }

    override fun handleCommonEvent(event: CommonChangeEvent<Event>) {
        if (event.type == CommonChangeEventType.CREATED) {
            val request = ItemEntitlementChangeRequest(event.obj.id, ItemType.EVENT, Entitlement.EDITOR)
            service.create(event.user!!, request)
        }
    }

}
