package de.sambalmueslie.oevent.common


import io.micronaut.data.repository.CrudRepository
import org.slf4j.Logger

abstract class BaseService<T : BusinessObject, R : BusinessObjectChangeRequest, D : DataObject<T>>(
		private val repository: CrudRepository<D, Long>,
		private val logger: Logger
) : BusinessObjectService<T, R> {

	override fun getAll(): List<T> {
		val result = repository.findAll()
		return getContext(result).map { it.first.convert(it.second) }
	}

	override fun get(id: Long): T? {
		val result = repository.findByIdOrNull(id) ?: return null
		return result.convert(getContext(result))
	}

	protected open fun getContext(data: D): DataObjectContext {
		return DataObjectContext.EMPTY
	}

	protected open fun getContext(data: Iterable<D>): List<Pair<D, DataObjectContext>> {
		return data.map { it to DataObjectContext.EMPTY }
	}

	override fun delete(id: Long) {
		repository.deleteById(id)
	}

	override fun create(request: R): T {
		val (data, dependencies) = persist(request)
		return data.convert(dependencies)
	}

	override fun update(id: Long, request: R): T {
		val existing = repository.findByIdOrNull(id) ?: return create(request)
		val (data, dependencies) = persist(request, existing)
		return data.convert(dependencies)
	}

	protected abstract fun getValidator(): DataObjectValidator<D>
	protected abstract fun getMerger(): DataObjectMerger<D, R>

	private fun persist(request: R, existing: D? = null): Pair<D, DataObjectContext> {
		val context = DataObjectContext()
		val merger = getMerger()
		val data = merger.merge(existing, request, context)
		getValidator().validate(data)
		return if (existing == null) {
			saveDependencies(data, request, context)
			val result = repository.save(data)
			saveRelations(result, context)
			Pair(result, context)
		} else {
			updateDependencies(data, request, context)
			val result = repository.update(data)
			updateRelations(result, context)
			Pair(result, context)
		}
	}

	protected open fun saveDependencies(data: D, request: R, context: DataObjectContext) {
		// intentionally left empty
	}

	protected open fun updateDependencies(data: D, request: R, context: DataObjectContext) {
		// intentionally left empty
	}

	protected open fun saveRelations(data: D, context: DataObjectContext) {
		// intentionally left empty
	}

	protected open fun updateRelations(data: D, context: DataObjectContext) {
		// intentionally left empty
	}
}
