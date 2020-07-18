package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity
import de.sambalmueslie.oevent.logic.category.db.CategoryItemRelationRepository
import de.sambalmueslie.oevent.logic.category.db.CategoryRepository
import de.sambalmueslie.oevent.logic.item.api.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class CategoryService(
		private val repository: CategoryRepository,
		private val relationRepository: CategoryItemRelationRepository
) : BaseService<Category, CategoryChangeRequest, CategoryEntity>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CategoryService::class.java)
	}

	private val validator = CategoryValidator()
	override fun getValidator() = validator

	private val merger = CategoryMerger()
	override fun getMerger() = merger

	fun get(name: String): Category? {
		return repository.findByName(name)?.convert()
	}

	fun get(item: Item): List<Category> {
		val relations = relationRepository.findByItemId(item.id)
		val categories = repository.findByIdInList(relations.map { it.categoryId })
//		val categories = repository.findByItem(item.id)
		return categories.map { it.convert() }
	}


}
