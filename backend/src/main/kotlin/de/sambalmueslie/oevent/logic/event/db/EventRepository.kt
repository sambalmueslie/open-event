package de.sambalmueslie.oevent.logic.event.db

import de.sambalmueslie.oevent.logic.item.db.ItemRepository
import de.sambalmueslie.oevent.model.AddressData
import de.sambalmueslie.oevent.model.ItemData
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import io.micronaut.data.repository.PageableRepository

@Repository
interface EventRepository : PageableRepository<EventEntity, Long> {
	fun findByShortText(shortText: String): List<EventEntity>
}
