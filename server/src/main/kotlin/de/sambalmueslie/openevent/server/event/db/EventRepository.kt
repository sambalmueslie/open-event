package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

interface EventRepository : PageableRepository<EventData, Long> {
	fun findExisting(user: User, request: EventChangeRequest): EventData?
}
