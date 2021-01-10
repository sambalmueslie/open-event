package de.sambalmueslie.openevent.server.category.db

import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import io.micronaut.data.repository.PageableRepository

interface CategoryRepository : PageableRepository<CategoryData, Long> {
	fun findExisting(request: CategoryChangeRequest): CategoryData?
	fun getAllForItem(itemId: Long): List<CategoryData>
	fun findByIdIn(categoryIds: Set<Long>): List<CategoryData>
}
