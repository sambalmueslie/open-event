package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EventService(
	private val crudService: EventCrudService
) : AuthCrudService<Event, EventChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Event> {
		// TODO check authentication
		return crudService.getAll(pageable)
	}

	override fun get(authentication: Authentication, user: User, eventId: Long): Event? {
		// TODO check authentication
		return crudService.get(eventId)
	}

	override fun create(authentication: Authentication, user: User, request: EventChangeRequest): Event? {
		// TODO check authentication
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, eventId: Long, request: EventChangeRequest): Event? {
		// TODO check authentication
		return crudService.update(user, eventId, request)
	}

	override fun delete(authentication: Authentication, user: User, eventId: Long) {
		// TODO check authentication
		return crudService.delete(user, eventId)
	}


}
