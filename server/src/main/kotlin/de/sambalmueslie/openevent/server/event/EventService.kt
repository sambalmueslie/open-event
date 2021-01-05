package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EventService(
	private val repository: EventRepository
) : AuthCrudService<Event, EventChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Event> {
		TODO("Not yet implemented")
	}

	override fun get(authentication: Authentication, user: User, eventId: Long): Event? {
		TODO("Not yet implemented")
	}

	override fun create(authentication: Authentication, user: User, request: EventChangeRequest): Event? {
		TODO("Not yet implemented")
	}

	override fun update(authentication: Authentication, user: User, eventId: Long, request: EventChangeRequest): Event? {
		TODO("Not yet implemented")
	}

	override fun delete(authentication: Authentication, user: User, eventId: Long) {
		TODO("Not yet implemented")
	}


}
