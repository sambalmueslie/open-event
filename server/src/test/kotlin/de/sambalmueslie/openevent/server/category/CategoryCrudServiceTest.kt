package de.sambalmueslie.openevent.server.category

import de.sambalmueslie.openevent.server.category.api.Category
import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.server.category.api.ItemCategories
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
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
}
