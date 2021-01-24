package de.sambalmueslie.openevent.server.messaging


import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/message")
class MessageController(
	userService: UserService,
	private val service: MessageService
) : CrudController<Message, MessageChangeRequest>(userService) {

	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{objId}")
	override fun get(authentication: Authentication, objId: Long) =
		service.get(authentication, getUser(authentication), objId)

	@Post()
	override fun create(authentication: Authentication, request: MessageChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{objId}")
	override fun update(authentication: Authentication, objId: Long, request: MessageChangeRequest) =
		service.update(authentication, getUser(authentication), objId, request)

	@Delete("/{objId}")
	override fun delete(authentication: Authentication, objId: Long) =
		service.delete(authentication, getUser(authentication), objId)


}
