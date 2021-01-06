package de.sambalmueslie.oevent.common


import io.micronaut.data.repository.CrudRepository
import org.ehcache.Cache
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.slf4j.Logger
import java.time.Duration

abstract class BaseService<T : BusinessObject, R : BusinessObjectChangeRequest, D : DataObject<T>>(
		private val repository: CrudRepository<D, Long>,
		private val logger: Logger
) : BusinessObjectService<T, R> {

	private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)

	protected fun <K, V> createCache(keyType: Class<K>, valueType: Class<V>, heapEntries: Long = 100, ttl: Duration = Duration.ofHours(1)): Cache<K, V> {
		val expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(ttl)
		val poolsBuilder = ResourcePoolsBuilder.heap(heapEntries)
		val cacheConfiguration = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(keyType, valueType, poolsBuilder)
				.withExpiry(expiryPolicy)
				.build()
		return cacheManager.createCache(valueType.simpleName, cacheConfiguration)
	}

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
		val data = repository.findByIdOrNull(id) ?: return
		repository.delete(data)
		deleteRelations(data)
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

	protected open fun deleteRelations(data: D) {
		// intentionally left empty
	}
}
