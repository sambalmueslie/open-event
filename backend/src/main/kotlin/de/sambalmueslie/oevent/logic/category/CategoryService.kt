package de.sambalmueslie.oevent.logic.category


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity
import de.sambalmueslie.oevent.logic.category.db.CategoryItemRelation
import de.sambalmueslie.oevent.logic.category.db.CategoryItemRelationRepository
import de.sambalmueslie.oevent.logic.category.db.CategoryRepository
import de.sambalmueslie.oevent.logic.item.api.Item
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ExpiryPolicyBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import javax.inject.Singleton


internal data class ItemCategories(val category: List<Category>)

@Singleton
class CategoryService(
		private val repository: CategoryRepository,
		private val relationRepository: CategoryItemRelationRepository
) : BaseService<Category, CategoryChangeRequest, CategoryEntity>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CategoryService::class.java)
	}

	private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)
	private val itemCategoryCache = cacheManager.createCache(ItemCategories::class.simpleName,
			CacheConfigurationBuilder.newCacheConfigurationBuilder(String::class.java, ItemCategories::class.java, ResourcePoolsBuilder.heap(100))
					.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1)))
					.build()
	)


	private val validator = CategoryValidator()
	override fun getValidator() = validator

	private val merger = CategoryMerger()
	override fun getMerger() = merger

	fun get(name: String): Category? {
		return repository.findByName(name)?.convert()
	}


	fun add(item: Item, category: Category): List<Category> {
		val id = CategoryItemRelation.createId(item, category)
		relationRepository.findByIdOrNull(id) ?: relationRepository.save(CategoryItemRelation(id, category.id, item.id))
		itemCategoryCache.remove(getItemCategoryCacheKey(item))
		return get(item)
	}

	fun remove(item: Item, category: Category): List<Category> {
		val id = CategoryItemRelation.createId(item, category)
		relationRepository.deleteById(id)
		itemCategoryCache.remove(getItemCategoryCacheKey(item))
		return get(item)
	}

	fun get(item: Item): List<Category> {
		val cacheKey = getItemCategoryCacheKey(item)
		val hit = itemCategoryCache.get(cacheKey)
		if(hit != null) return hit.category

		val categories = repository.findByItem(item.id)
		val result =  categories.map { it.convert() }
		itemCategoryCache.put(cacheKey, ItemCategories(result))
		return result
	}

	private fun getItemCategoryCacheKey(item: Item) = "${item.id}"


}
