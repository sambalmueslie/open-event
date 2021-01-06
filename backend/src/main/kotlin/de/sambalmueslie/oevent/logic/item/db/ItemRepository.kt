package de.sambalmueslie.oevent.logic.item.db

import de.sambalmueslie.oevent.model.ItemData
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import io.micronaut.data.repository.PageableRepository

@Repository
interface ItemRepository : PageableRepository<ItemEntity, Long> {
	fun findByShortText(shortText: String): List<ItemEntity>
}
