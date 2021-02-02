package de.sambalmueslie.openevent.server.messaging.actions


import de.sambalmueslie.openevent.server.common.*
import de.sambalmueslie.openevent.server.messaging.MessageCrudService
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.messaging.db.MessageConvertContent
import de.sambalmueslie.openevent.server.messaging.db.MessageRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class MarkRepliedAction(
	private val service: MessageCrudService,
	private val repository: MessageRepository,
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(MarkRepliedAction::class.java)
	}

	init {
		service.register(object : BusinessObjectChangeListener<Message> {
			override fun handleCommonEvent(event: CommonChangeEvent<Message>) {
				if (event.type == CommonChangeEventType.CREATED) handleCreatedEvent(event)
			}
		})
	}

	private fun handleCreatedEvent(event: CommonChangeEvent<Message>) {
		val message = event.obj
		val parentMessageId = message.header.parentMessageId ?: return

		val parent = repository.findByIdOrNull(parentMessageId) ?: return
		parent.markReplied()
		service.update(event.user!!, parent)
	}

}
