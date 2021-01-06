package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.model.CategoryData
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity

class CategoryMerger : DataObjectMerger<CategoryEntity, CategoryChangeRequest> {

	override fun merge(existing: CategoryEntity?, request: CategoryChangeRequest, context: DataObjectContext): CategoryEntity {
		val data = existing ?: CategoryEntity()
		data.name = request.name
		data.iconUrl = request.iconUrl
		return data
	}
}
