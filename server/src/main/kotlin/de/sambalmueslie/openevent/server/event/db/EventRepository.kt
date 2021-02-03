package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.item.db.ItemRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.annotation.Id
import io.micronaut.data.repository.PageableRepository

interface EventRepository : ItemRepository<EventData, EventChangeRequest> {
	fun updatePublished(@Id id: Long, published: Boolean)
}
