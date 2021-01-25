package de.sambalmueslie.openevent.server.messaging


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.db.MessageConvertContent
import de.sambalmueslie.openevent.server.messaging.db.MessageData
import de.sambalmueslie.openevent.server.messaging.db.MessageRepository
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class MessageCrudService(
	private val repository: MessageRepository,
	private val userService: UserService,
) : BaseCrudService<Message, MessageChangeRequest, MessageData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(MessageCrudService::class.java)
	}

	override fun convert(data: MessageData): Message {
		val author = userService.getUser(data.authorId)!!
		val recipient = userService.getUser(data.recipientId)!!
		return data.convert(MessageConvertContent(author, recipient))
	}

	override fun create(user: User, request: MessageChangeRequest): Message? {
		val recipient = userService.getUser(request.recipientId) ?: return null
		val data = MessageData.convert(user, request)
		val result = repository.save(data).convert(MessageConvertContent(user, recipient))
		notifyCreated(user, result)
		return result
	}

	override fun update(user: User, objId: Long, request: MessageChangeRequest): Message? {
		return get(objId)
	}

	override fun update(user: User, obj: MessageData, request: MessageChangeRequest): Message {
		return convert(obj)
	}

	fun update(user: User, obj: MessageData): Message {
		val result = convert(repository.save(obj))
		notifyUpdated(user, result)
		return result
	}

}
