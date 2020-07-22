package de.sambalmueslie.oevent.logic.item.relation

import io.micronaut.data.repository.CrudRepository

interface ItemEntityRelationRepository<T : ItemEntityRelation> : CrudRepository<T, String> {
	fun findByEntityId(entityId: Long): List<T>
	fun findByItemId(itemId: Long): List<T>
	fun deleteByItemId(itemId: Long)
	fun deleteByEntityId(entityId: Long)
}
