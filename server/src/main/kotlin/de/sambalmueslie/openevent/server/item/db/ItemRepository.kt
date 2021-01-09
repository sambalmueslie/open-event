package de.sambalmueslie.openevent.server.item.db

import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.repository.PageableRepository

interface ItemRepository<E> : PageableRepository<E, Long> {
	fun findExisting(user: User, request: ItemChangeRequest): E?
}
