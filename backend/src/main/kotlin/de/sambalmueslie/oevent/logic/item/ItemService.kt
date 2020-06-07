package de.sambalmueslie.oevent.logic.item


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.item.db.ItemData
import de.sambalmueslie.oevent.logic.item.db.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ItemService(private val repository: ItemRepository) : BaseService<Item, ItemChangeRequest, ItemData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(ItemService::class.java)
	}

	private val validator = ItemValidator()
	private val merger = ItemMerger()

	override fun getMerger() = merger
	override fun getValidator() = validator

	fun get(shortText: String): List<Item> {
		return repository.findByShortText(shortText).map { it.convert() }
	}


}
