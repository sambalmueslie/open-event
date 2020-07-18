package de.sambalmueslie.oevent.logic.category.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface CategoryRepository : CrudRepository<CategoryEntity, Long> {
	fun findByName(name: String): CategoryEntity?
	fun findByIdInList(ids: List<Long>): List<CategoryEntity>

//	@Query("FROM Category c, CategoryItemRelation r JOIN r.categoryId = c.id")
//	fun findByItem(itemId: Long): List<CategoryEntity>
}
