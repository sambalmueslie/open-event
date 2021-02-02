package de.sambalmueslie.openevent.server.messaging


import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessagingAPI
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/message")
class MessageController(
	userService: UserService,
	private val service: MessageService
) : CrudController<Message, MessageChangeRequest>(userService), MessagingAPI {

	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{objId}")
	override fun get(authentication: Authentication, @PathVariable objId: Long) =
		service.get(authentication, getUser(authentication), objId)

	@Post()
	override fun create(authentication: Authentication, @Body request: MessageChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{objId}")
	override fun update(authentication: Authentication, @PathVariable objId: Long, @Body request: MessageChangeRequest) =
		service.update(authentication, getUser(authentication), objId, request)

	@Delete("/{objId}")
	override fun delete(authentication: Authentication, @PathVariable objId: Long) =
		service.delete(authentication, getUser(authentication), objId)


	@Get("/received")
	override fun getReceivedMessages(authentication: Authentication, pageable: Pageable) =
		service.getReceivedMessages(authentication, getUser(authentication), pageable)

	@Get("/sent")
	override fun getSentMessages(authentication: Authentication, pageable: Pageable) =
		service.getSentMessages(authentication, getUser(authentication), pageable)

	@Get("/unread/count")
	override fun getUnreadMessageCount(authentication: Authentication) =
		service.getUnreadMessageCount(authentication, getUser(authentication))

	@Get("/unread")
	override fun getUnreadMessages(authentication: Authentication, pageable: Pageable) =
		service.getUnreadMessages(authentication, getUser(authentication), pageable)

	@Put("/{messageId}/read")
	override fun markRead(authentication: Authentication, @PathVariable messageId: Long) =
		service.markRead(authentication, getUser(authentication), messageId)

	@Delete()
	fun deleteAll(authentication: Authentication) = service.deleteAll(authentication, getUser(authentication))
}
