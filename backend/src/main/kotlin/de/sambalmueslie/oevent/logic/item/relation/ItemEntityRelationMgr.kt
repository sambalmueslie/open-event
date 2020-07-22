package de.sambalmueslie.oevent.logic.item.relation

import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.item.api.Item
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import java.time.Duration

class ItemEntityRelationMgr<T : ItemEntityRelation, E : BusinessObject, D : DataObject<E>>(
		private val repository: EntityRepository<E, D>,
		private val relationRepository: ItemEntityRelationRepository<T>,
		type: Class<E>,
		private val factory: (id: String, item: Item, entity: E) -> T
) : ItemEntityRelationService<E> {
	private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)
	private val expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1))
	private val poolsBuilder = ResourcePoolsBuilder.heap(100)
	private val cacheConfiguration = CacheConfigurationBuilder
			.newCacheConfigurationBuilder(String::class.java, ItemEntities::class.java, poolsBuilder)
			.withExpiry(expiryPolicy)
			.build()
	private val cache = cacheManager.createCache("Item${type.simpleName}Relation", cacheConfiguration)

	override fun add(item: Item, entity: E): List<E> {
		val id = createId(item, entity)
		relationRepository.findByIdOrNull(id) ?: relationRepository.save(factory.invoke(id, item, entity))
		cache.remove(getCacheKey(item))
		return get(item)
	}

	override fun remove(item: Item, entity: E): List<E> {
		val id = createId(item, entity)
		relationRepository.deleteById(id)
		cache.remove(getCacheKey(item))
		return get(item)
	}

	override fun get(item: Item): List<E> {
		return getByItemId(item.id)
	}

	@Suppress("UNCHECKED_CAST")
	override fun getByItemId(itemId: Long): List<E> {
		val cacheKey = getCacheKey(itemId)
		val hit = cache.get(cacheKey)
		if (hit != null) return hit.entities as List<E>

		val categories = repository.findByItem(itemId)
		val result = categories.map { it.convert() }
		cache.put(cacheKey, ItemEntities(result))
		return result
	}

	fun delete(data: DataObject<E>) {
		val entries = relationRepository.findByEntityId(data.id)
		if (entries.isEmpty()) return
		relationRepository.deleteAll(entries)
		entries.groupBy { it.itemId }
				.map { getCacheKey(it.key) }
				.forEach { cache.remove(it) }
	}

	private fun createId(item: Item, entity: E) = "${item.id}#${entity.id}"
	private fun getCacheKey(item: Item) = getCacheKey(item.id)
	private fun getCacheKey(itemId: Long) = "$itemId"
}
