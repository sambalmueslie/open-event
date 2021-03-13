package de.sambalmueslie.openevent.server.event.actions


import de.sambalmueslie.openevent.server.common.Action
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.config.EventConfig
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.db.EventRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AutoPublishAction(
        private val repository: EventRepository,
        private val config: EventConfig,
) : Action<Event> {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(AutoPublishAction::class.java)
    }


    override fun handleCommonEvent(event: CommonChangeEvent<Event>) {
        if (event.type == CommonChangeEventType.CREATED) handleCreatedEvent(event)
    }

    private fun handleCreatedEvent(event: CommonChangeEvent<Event>) {
        if (config.autoPublish) {
            repository.updatePublished(event.obj.id, true)
        } else if (event.obj.published) {
            repository.updatePublished(event.obj.id, false)
        }
    }


}
