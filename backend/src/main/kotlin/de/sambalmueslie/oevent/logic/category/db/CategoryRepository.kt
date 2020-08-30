package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.item.relation.EntityRepository
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface CategoryRepository : CrudRepository<CategoryEntity, Long>, EntityRepository<Category, CategoryEntity> {
	fun findByName(name: String): CategoryEntity?
	@Query(value = "SELECT c FROM Category c, CategoryItemRelation r WHERE r.categoryId = c.id")
	override fun findByItem(itemId: Long): List<CategoryEntity>
}
