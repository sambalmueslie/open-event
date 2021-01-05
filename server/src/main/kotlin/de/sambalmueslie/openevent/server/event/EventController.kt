package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/api/event")
class EventController(
	userService: UserService,
	private val service: EventService
) : CrudController<Event, EventChangeRequest>(userService) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventController::class.java)
	}

	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{eventId}")
	override fun get(authentication: Authentication, @PathVariable eventId: Long) =
		service.get(authentication, getUser(authentication), eventId)

	@Post()
	override fun create(authentication: Authentication, @Body request: EventChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{eventId}")
	override fun update(authentication: Authentication, @PathVariable eventId: Long, @Body request: EventChangeRequest) =
		service.update(authentication, getUser(authentication), eventId, request)

	@Delete("/{eventId}")
	override fun delete(authentication: Authentication, @PathVariable eventId: Long) =
		service.delete(authentication, getUser(authentication), eventId)

}
