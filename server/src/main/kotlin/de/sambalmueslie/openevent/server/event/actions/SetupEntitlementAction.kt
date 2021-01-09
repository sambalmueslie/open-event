package de.sambalmueslie.openevent.server.event.actions


import de.sambalmueslie.openevent.server.common.BusinessObjectChangeEvent
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.event.EventCrudService
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.item.api.ItemType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class SetupEntitlementAction(
	service: EventCrudService,
	private val entitlementService: ItemEntitlementCrudService,
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(SetupEntitlementAction::class.java)
	}

	init {
		service.register(object : BusinessObjectChangeListener<Event> {
			override fun <O : Any> handleChangeEvent(event: BusinessObjectChangeEvent<Event, O>) {
				// intentionally left empty
			}

			override fun handleCommonEvent(event: CommonChangeEvent<Event>) {
				if (event.type == CommonChangeEventType.CREATED) handleCreatedEvent(event)
			}
		})
	}

	private fun handleCreatedEvent(event: CommonChangeEvent<Event>) {
		val request = ItemEntitlementChangeRequest(event.obj.id, ItemType.EVENT, Entitlement.EDITOR)
		entitlementService.create(event.user!!, request)
	}
}
