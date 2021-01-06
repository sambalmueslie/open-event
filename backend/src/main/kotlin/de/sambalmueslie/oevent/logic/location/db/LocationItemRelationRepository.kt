package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationRepository
import io.micronaut.data.annotation.Repository

@Repository
interface LocationItemRelationRepository : ItemEntityRelationRepository<LocationItemRelation> {
	override fun findByEntityId(entityId: Long): List<LocationItemRelation>
	override fun findByItemId(itemId: Long): List<LocationItemRelation>
	override fun deleteByItemId(itemId: Long)
	override fun deleteByEntityId(entityId: Long)
}
