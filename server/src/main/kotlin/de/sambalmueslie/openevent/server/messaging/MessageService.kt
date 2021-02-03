package de.sambalmueslie.openevent.server.messaging


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.messaging.db.MessageData
import de.sambalmueslie.openevent.server.messaging.db.MessageRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class MessageService(
	private val repository: MessageRepository,
	private val crudService: MessageCrudService,
	private val authHelper: AuthenticationHelper
) : AuthCrudService<Message, MessageChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(MessageService::class.java)
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Message> {
		return if (authHelper.isAdmin(authentication)) {
			crudService.getAll(pageable)
		} else {
			repository.findByAuthorIdOrRecipientId(user.id, user.id, pageable).map { crudService.convert(it) }
		}
	}

	override fun get(authentication: Authentication, user: User, objId: Long): Message? {
		val message = crudService.get(objId) ?: return null
		return if (authHelper.isAdmin(authentication) || hasReadAccess(user, message)) message else null
	}

	override fun create(authentication: Authentication, user: User, request: MessageChangeRequest): Message? {
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, objId: Long, request: MessageChangeRequest): Message? {
		return if (authHelper.isAdmin(authentication)) {
			crudService.update(user, objId, request)
		} else {
			val message = crudService.getData(objId) ?: return create(authentication, user, request)
			return if (hasWriteAccess(user, message))  crudService.update(user, message, request) else null
		}
	}

	override fun delete(authentication: Authentication, user: User, objId: Long) {
		if (authHelper.isAdmin(authentication)) {
			crudService.delete(user, objId)
		} else {
			val message = crudService.getData(objId) ?: return
			if (hasWriteAccess(user, message)) crudService.delete(user, message)
		}
	}

	private fun hasReadAccess(user: User, message: Message): Boolean {
		val header = message.header
		val isAuthor = header.author.id == user.id
		val isRecipient = header.recipient.id == user.id
		return isAuthor || isRecipient
	}

	private fun hasWriteAccess(user: User, message: MessageData): Boolean {
		return message.authorId == user.id
	}

	fun markRead(authentication: Authentication, user: User, messageId: Long): Message? {
		val message = crudService.getData(messageId) ?: return null
		return if (message.recipientId == user.id) {
			message.markRead()
			crudService.update(user, message)
		} else {
			crudService.convert(message)
		}
	}

	fun getReceivedMessages(authentication: Authentication, user: User, pageable: Pageable): Page<Message> {
		return repository.findByRecipientId(user.id, pageable).map { crudService.convert(it) }
	}

	fun getSentMessages(authentication: Authentication, user: User, pageable: Pageable): Page<Message> {
		return repository.findByAuthorId(user.id, pageable).map { crudService.convert(it) }
	}

	fun getUnreadMessages(authentication: Authentication, user: User, pageable: Pageable): Page<Message> {
		return repository.findByRecipientIdAndStatus(user.id, MessageStatus.CREATED, pageable).map { crudService.convert(it) }
	}

	fun getUnreadMessageCount(authentication: Authentication, user: User): Int {
		return repository.countByRecipientIdAndStatus(user.id, MessageStatus.CREATED)
	}

	fun deleteAll(authentication: Authentication, user: User) {
		if (authHelper.isAdmin(authentication)) {
			repository.deleteAll()
		}
	}

}
