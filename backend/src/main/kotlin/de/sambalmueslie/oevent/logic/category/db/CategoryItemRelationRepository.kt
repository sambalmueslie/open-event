package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationRepository
import io.micronaut.data.annotation.Repository

@Repository
interface CategoryItemRelationRepository : ItemEntityRelationRepository<CategoryItemRelation> {
	override fun findByEntityId(entityId: Long): List<CategoryItemRelation>
	override fun findByItemId(itemId: Long): List<CategoryItemRelation>
	override fun deleteByItemId(itemId: Long)
}
