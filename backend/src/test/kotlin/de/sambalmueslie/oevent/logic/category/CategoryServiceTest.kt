package de.sambalmueslie.oevent.logic.category

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import de.sambalmueslie.oevent.logic.item.api.Item
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import javax.inject.Inject

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@MicronautTest
internal class CategoryServiceTest {

	companion object {
		private var idCtr = 0L
	}

	@Inject
	lateinit var service: CategoryService

	@Test
	fun `create update get and delete a category`() {
		val name = "Test name"
		val url = "Test URL"
		idCtr++
		assertEquals(Category(idCtr, name, url), service.create(CategoryChangeRequest(name, url)))

		assertEquals(listOf(Category(idCtr, name, url)), service.getAll())
		assertEquals(Category(idCtr, name, url), service.get(idCtr))
		assertEquals(Category(idCtr, name, url), service.get(name))

		val altName = "Alt Name"
		val altUrl = "Alt Url"
		assertEquals(Category(idCtr, altName, altUrl), service.update(idCtr, CategoryChangeRequest(altName, altUrl)))
	}

	@Test
	fun `test category item relations`() {
		val name = "Test name"
		val url = "Test URL"
		val category = service.create(CategoryChangeRequest(name, url))
		idCtr++
		assertEquals(Category(idCtr, name, url), category)

		val item = object : Item {
			override val id = 1L
			override val title: String = "Title"
			override val shortText: String = "shortText"
			override val longText: String = "longText"
			override val imageUrl: String = "imageUrl"
			override val iconUrl: String = "iconUrl"
		}
		assertEquals(listOf(category), service.add(item, category))
		assertEquals(listOf(category), service.get(item))
		assertEquals(listOf(category), service.get(item)) // test cache
		assertEquals(emptyList<Category>(), service.remove(item, category))
		assertEquals(emptyList<Category>(), service.get(item))
	}

}
