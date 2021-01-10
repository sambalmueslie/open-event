package de.sambalmueslie.openevent.server.category.db

import de.sambalmueslie.openevent.server.category.api.Category
import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "CategoryItemRelation")
@Table(name = "category_item_relation")
data class CategoryItemRelationData(
	@Column(nullable = false)
	var categoryId: Long = 0L,
	@Column(nullable = false)
	var itemId: Long = 0L
) {
	companion object {
		fun convert(category: Category, request: CategoryChangeRequest) = CategoryItemRelationData(category.id, request.itemId)
	}
}
