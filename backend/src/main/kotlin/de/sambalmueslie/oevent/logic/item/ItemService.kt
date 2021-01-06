package de.sambalmueslie.oevent.logic.item


import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.item.db.ItemEntity
import de.sambalmueslie.oevent.logic.item.db.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ItemService(private val repository: ItemRepository) {


	companion object {
		val logger: Logger = LoggerFactory.getLogger(ItemService::class.java)
	}


	fun get(shortText: String): List<Item> {
		return repository.findByShortText(shortText)
	}

	fun <T : ItemEntity> merge(request: ItemChangeRequest, data: T): T {
		data.iconUrl = request.iconUrl
		data.imageUrl = request.imageUrl
		data.longText = request.longText
		data.shortText = request.shortText
		data.title = request.title
		return data
	}

}
