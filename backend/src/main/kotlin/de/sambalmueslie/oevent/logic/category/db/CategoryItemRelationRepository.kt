package de.sambalmueslie.oevent.logic.category.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface CategoryItemRelationRepository : CrudRepository<CategoryItemRelation, Long> {
	fun findByCategoryId(categoryId: Long): List<CategoryItemRelation>
	fun findByItemId(itemId: Long): List<CategoryItemRelation>
}
