package de.sambalmueslie.oevent.logic.item

import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.item.db.ItemEntity

class ItemMerger<E : ItemEntity> {

	fun merge(entity: E, request: ItemChangeRequest) {
		entity.title = request.title
		entity.shortText = request.shortText
		entity.longText = request.longText
		entity.iconUrl = request.iconUrl
		entity.imageUrl = request.imageUrl
	}

}
