package de.sambalmueslie.openevent.server.category

import de.sambalmueslie.openevent.server.category.api.Category
import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.server.category.api.ItemCategories
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class CategoryCrudServiceTest(
	userRepo: UserRepository,
	private val service: CategoryCrudService
) {
	private val user: UserData = UserUtils.getUser(userRepo)
	private val name = "Test Name"
	private val iconUrl = "Test Icon Url"
	private val itemId = 10L

	@Test
	fun `create update and delete category`() {
		val u = user.convert()

		val createResult = service.create(u, CategoryChangeRequest(name, iconUrl, itemId))
		val objId = createResult.id
		val category = Category(objId, name, iconUrl)
		assertEquals(category, createResult)

		val itemCategories = service.getItemCategories(itemId)
		assertEquals(ItemCategories(itemId, setOf(category)), itemCategories)

		val updateName = "changed name"
		val updateResult = service.update(u, category.id, CategoryChangeRequest(updateName, iconUrl, itemId))
		val updateCategory = Category(objId, updateName, iconUrl)
		assertEquals(updateCategory, updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(updateCategory, getResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(updateCategory), getAllResult.content)

		service.delete(u, objId)
		assertEquals(null, service.get(objId))
		assertEquals(ItemCategories(itemId, emptySet()), service.getItemCategories(itemId))
	}

	@Test
	fun `create while existing`() {
		val u = user.convert()
		val createResult = service.create(u, CategoryChangeRequest(name, iconUrl, itemId))
		val objId = createResult.id
		val category = Category(objId, name, iconUrl)
		assertEquals(category, createResult)

		val updatedIconUrl = "other icon url"
		val updateResult = service.create(u, CategoryChangeRequest(name, updatedIconUrl, itemId))
		assertEquals(Category(objId, name, updatedIconUrl), updateResult)
		service.delete(u, objId)
	}

	@Test
	fun `update with unknown id`() {
		val u = user.convert()
		val createResult = service.update(u, 10L, CategoryChangeRequest(name, iconUrl, itemId))
		assertNotNull(createResult)
		val objId = createResult!!.id
		val category = Category(objId, name, iconUrl)
		assertEquals(category, createResult)
		service.delete(u, objId)
	}


	@Test
	fun `update duplicate`() {
		val u = user.convert()
		val createFirstResult = service.create(u, CategoryChangeRequest(name, iconUrl, itemId))
		val updateName = "changed name"
		val createSecondResult = service.create(u, CategoryChangeRequest(updateName, iconUrl, itemId))
		assertNotEquals(createFirstResult, createSecondResult)

		val updateResult = service.update(u, createFirstResult.id, CategoryChangeRequest(updateName, iconUrl, itemId))
		assertNull(updateResult)
		service.delete(u, createFirstResult.id)
		service.delete(u, createSecondResult.id)
	}


	@Test
	fun `update with different item id`() {
		val u = user.convert()
		val createResult = service.create(u, CategoryChangeRequest(name, iconUrl, itemId))
		val otherItemId = itemId + 1
		val updateResult = service.update(u, createResult.id, CategoryChangeRequest(name, iconUrl, otherItemId))
		assertEquals(createResult, updateResult)

		val objId = createResult.id
		val category = Category(objId, name, iconUrl)
		assertEquals(ItemCategories(itemId, setOf(category)), service.getItemCategories(itemId))
		assertEquals(ItemCategories(otherItemId, setOf(category)), service.getItemCategories(otherItemId))

		service.delete(u, createResult.id)
	}

	@Test
	fun `create and assign or revoke items`() {
		val u = user.convert()

		val firstCategory = service.create(u, CategoryChangeRequest("first", iconUrl, itemId))
		val secondCategory = service.create(u, CategoryChangeRequest("second", iconUrl, itemId))

		val otherItemId = itemId + 1
		val assignResult = service.itemAssign(u, otherItemId, setOf(firstCategory.id, secondCategory.id))
		assertEquals(ItemCategories(otherItemId, setOf(firstCategory, secondCategory)), assignResult)
		assertEquals(ItemCategories(itemId, setOf(firstCategory, secondCategory)), service.getItemCategories(itemId))

		val revokeResult = service.itemRevoke(u, itemId, setOf(firstCategory.id))
		assertEquals(ItemCategories(itemId, setOf(secondCategory)), revokeResult)
		assertEquals(ItemCategories(otherItemId, setOf(firstCategory, secondCategory)), service.getItemCategories(otherItemId))

		service.itemRevokeAll(u, otherItemId)
		assertEquals(ItemCategories(itemId, setOf(secondCategory)), service.getItemCategories(itemId))
		assertEquals(ItemCategories(otherItemId, emptySet()), service.getItemCategories(otherItemId))

		val setResult = service.itemSet(u, itemId, setOf(firstCategory.id))
		assertEquals(ItemCategories(itemId, setOf(firstCategory)), setResult)
	}
}
