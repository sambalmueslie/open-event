package de.sambalmueslie.oevent.logic.item


import de.sambalmueslie.oevent.logic.common.DataObjectContext
import de.sambalmueslie.oevent.logic.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.item.db.ItemData

class ItemMerger : DataObjectMerger<ItemData, ItemChangeRequest> {

	override fun merge(existing: ItemData?, request: ItemChangeRequest, context: DataObjectContext): ItemData {
		val data = existing ?: ItemData()
		data.iconUrl = request.iconUrl
		data.imageUrl = request.imageUrl
		data.longText = request.longText
		data.shortText = request.shortText
		data.title = request.title
		return data
	}

}
