package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryData
import de.sambalmueslie.oevent.logic.category.db.CategoryRepository
import de.sambalmueslie.oevent.common.BaseService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class CategoryService(private val repository: CategoryRepository) : BaseService<Category, CategoryChangeRequest, CategoryData>(repository, logger) {

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


}
