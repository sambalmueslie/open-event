package de.sambalmueslie.openevent.server.category


import de.sambalmueslie.openevent.server.category.api.Category
import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.server.category.api.ItemCategories
import de.sambalmueslie.openevent.server.category.db.CategoryData
import de.sambalmueslie.openevent.server.category.db.CategoryItemRelationData
import de.sambalmueslie.openevent.server.category.db.CategoryItemRelationRepository
import de.sambalmueslie.openevent.server.category.db.CategoryRepository
import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class CategoryCrudService(
	private val repository: CategoryRepository,
	private val relationRepository: CategoryItemRelationRepository
) : BaseCrudService<Category, CategoryChangeRequest, CategoryData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CategoryCrudService::class.java)
	}

	override fun convert(data: CategoryData): Category {
		return data.convert()
	}

	override fun create(user: User, request: CategoryChangeRequest): Category {
		val existing = repository.findExisting(request)
		if (existing != null) return update(user, existing.id, request) ?: existing.convert()
		val data = CategoryData.convert(request)
		val result = repository.save(data).convert()
		createRelation(user, result, request)
		notifyCreated(user, result)
		return result
	}

	override fun update(user: User, objId: Long, request: CategoryChangeRequest): Category? {
		val existing = repository.findByIdOrNull(objId) ?: return create(user, request)
		return update(user, existing, request)
	}

	override fun update(user: User, obj: CategoryData, request: CategoryChangeRequest): Category? {
		val duplicate = repository.findExisting(request)
		if (duplicate != null && duplicate.id != obj.id) return null
		obj.update(request)
		val result = repository.update(obj).convert()

		val existingRelation = relationRepository.findByCategoryIdAndItemId(obj.id, request.itemId)
		if (existingRelation == null) {
			createRelation(user, result, request)
		}

		notifyUpdated(user, result)
		return result
	}

	private fun createRelation(user: User, result: Category, request: CategoryChangeRequest) {
		val relation = CategoryItemRelationData.convert(result, request)
		relationRepository.save(relation)
		notifyRelationCreated(user, relation)
	}


	override fun prepareDeletion(user: User, data: CategoryData) {
		val existing = relationRepository.findByCategoryId(data.id)
		if (existing.isEmpty()) return
		relationRepository.deleteAll(existing)
		existing.forEach { notifyRelationDeleted(user, it) }
	}

	fun getItemCategories(itemId: Long): ItemCategories {
		val categories = repository.getAllForItem(itemId).map { it.convert() }.toSet()
		return ItemCategories(itemId, categories)
	}

	fun itemAssign(user: User, itemId: Long, categoryIds: Set<Long>): ItemCategories {
		val categories = repository.findByIdIn(categoryIds)
		val existingRelations = relationRepository.findByItemId(itemId).map { it.categoryId }.toSet()
		val relationsToAdd = categories.filterNot { existingRelations.contains(it.id) }.map { CategoryItemRelationData(it.id, itemId) }
		relationsToAdd.forEach { notifyRelationCreated(user, it) }
		relationRepository.saveAll(relationsToAdd)
		return getItemCategories(itemId)
	}

	fun itemSet(user: User, itemId: Long, categoryIds: Set<Long>): ItemCategories {
		val categories = repository.findByIdIn(categoryIds)
		val existingRelations = relationRepository.findByItemId(itemId).map { it.categoryId to it }.toMap()
		val relationsToAdd = categories.filterNot { existingRelations.containsKey(it.id) }.map { CategoryItemRelationData(it.id, itemId) }
		relationsToAdd.forEach { notifyRelationCreated(user, it) }
		relationRepository.saveAll(relationsToAdd)
		val relationsToDelete = existingRelations.entries.filterNot { categoryIds.contains(it.key) }.map { it.value }
		relationsToDelete.forEach { notifyRelationDeleted(user, it) }
		relationRepository.deleteAll(relationsToDelete)
		return getItemCategories(itemId)
	}

	fun itemRevoke(user: User, itemId: Long, categoryIds: Set<Long>): ItemCategories {
		val existingRelations = relationRepository.findByItemId(itemId)
		val relationsToDelete = existingRelations.filter { categoryIds.contains(it.categoryId) }
		relationsToDelete.forEach { notifyRelationDeleted(user, it) }
		relationRepository.deleteAll(relationsToDelete)
		return getItemCategories(itemId)
	}


	fun itemRevokeAll(user: User, itemId: Long) {
		val existing = relationRepository.findByItemId(itemId)
		if (existing.isEmpty()) return
		relationRepository.deleteAll(existing)
		existing.forEach { notifyRelationDeleted(user, it) }
	}

	private fun notifyRelationCreated(user: User, data: CategoryItemRelationData) {
		// TODO send notification for created relations
	}

	private fun notifyRelationDeleted(user: User, data: CategoryItemRelationData) {
		// TODO send notification for deleted relations
	}


}
