package de.sambalmueslie.oevent.logic.category


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryData
import de.sambalmueslie.oevent.logic.category.db.CategoryRepository
import de.sambalmueslie.oevent.logic.common.BusinessObjectService
import de.sambalmueslie.oevent.logic.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.period.api.Period
import de.sambalmueslie.oevent.logic.period.api.PeriodChangeRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class CategoryService(private val repository: CategoryRepository) : BusinessObjectService<Category, CategoryChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CategoryService::class.java)
	}

	override fun getAll(): List<Category> {
		return repository.findAll().map { it.convert() }
	}

	fun get(name: String): Category? {
		return repository.findByName(name)?.convert()
	}

	override fun get(id: Long): Category? {
		return repository.findByIdOrNull(id)?.convert()
	}

	@Throws(InvalidRequestStateException::class)
	override fun create(request: CategoryChangeRequest): Category {
		val existing = repository.findByName(request.name)
		if (existing != null) return existing.convert()

		return persist(convert(request)).convert()
	}

	@Throws(InvalidRequestStateException::class)
	override fun update(id: Long, request: CategoryChangeRequest): Category {
		val existing = repository.findByIdOrNull(id) ?: return create(request)

		existing.name = request.name
		existing.iconUrl = request.iconUrl
		return persist(existing).convert()
	}

	override fun delete(id: Long) {
		repository.deleteById(id)
	}

	private fun convert(request: CategoryChangeRequest): CategoryData {
		return CategoryData(0, request.name, request.iconUrl)
	}

	private fun persist(data: CategoryData): CategoryData {
		validate(data)
		return if (data.id == 0L) {
			repository.save(data)
		} else {
			repository.update(data)
		}
	}

	private fun validate(data: CategoryData) {
		if (data.name.isBlank()) throw InvalidRequestStateException("Name of category ${data.id} must not be blank.")
	}

}
