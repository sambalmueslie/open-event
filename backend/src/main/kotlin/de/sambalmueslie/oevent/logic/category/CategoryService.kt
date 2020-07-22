package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity
import de.sambalmueslie.oevent.logic.category.db.CategoryItemRelation
import de.sambalmueslie.oevent.logic.category.db.CategoryItemRelationRepository
import de.sambalmueslie.oevent.logic.category.db.CategoryRepository
import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationMgr
import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class CategoryService(
		private val repository: CategoryRepository,
		relationRepository: CategoryItemRelationRepository
) : BaseService<Category, CategoryChangeRequest, CategoryEntity>(repository, logger), ItemEntityRelationService<Category> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CategoryService::class.java)
	}

	private val factory = { id: String, i: Item, e: Category -> CategoryItemRelation(id, e.id, i.id) }
	private val relationService = ItemEntityRelationMgr(repository, relationRepository, Category::class.java, factory)

	private val validator = CategoryValidator()
	override fun getValidator() = validator

	private val merger = CategoryMerger()
	override fun getMerger() = merger

	fun get(name: String): Category? = repository.findByName(name)?.convert()

	override fun add(item: Item, entity: Category): List<Category> = relationService.add(item, entity)
	override fun remove(item: Item, entity: Category): List<Category> = relationService.remove(item, entity)
	override fun get(item: Item): List<Category> = relationService.get(item)
	override fun getByItemId(itemId: Long): List<Category> = relationService.getByItemId(itemId)

	override fun deleteRelations(data: CategoryEntity) {
		relationService.delete(data)
	}


}
