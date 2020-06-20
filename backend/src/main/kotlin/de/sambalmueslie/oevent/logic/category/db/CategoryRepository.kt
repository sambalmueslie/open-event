package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.model.CategoryData
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface CategoryRepository : CrudRepository<CategoryData, Long> {
	fun findByName(name: String): CategoryData?
}
