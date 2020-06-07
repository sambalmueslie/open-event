package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryData
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger

class CategoryMerger : DataObjectMerger<CategoryData, CategoryChangeRequest> {

	override fun merge(existing: CategoryData?, request: CategoryChangeRequest, context: DataObjectContext): CategoryData {
		val data = existing ?: CategoryData()
		data.name = request.name
		data.iconUrl = request.iconUrl
		return data
	}
}
