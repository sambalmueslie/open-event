package de.sambalmueslie.oevent.logic.item.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ItemRepository : CrudRepository<ItemData, Long> {
	fun findByShortText(shortText: String): List<ItemData>
}
