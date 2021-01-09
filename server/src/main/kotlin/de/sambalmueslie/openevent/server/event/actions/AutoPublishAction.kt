package de.sambalmueslie.openevent.server.event.actions


import de.sambalmueslie.openevent.server.common.BusinessObjectChangeEvent
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.config.EventConfig
import de.sambalmueslie.openevent.server.event.EventCrudService
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.db.EventRepository
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Context
class AutoPublishAction(
	service: EventCrudService,
	private val repository: EventRepository,
	private val config: EventConfig,
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AutoPublishAction::class.java)
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
		if (config.autoPublish) {
			repository.updatePublished(event.obj.id, true)
		} else if (event.obj.published) {
			repository.updatePublished(event.obj.id, false)
		}
	}


}
