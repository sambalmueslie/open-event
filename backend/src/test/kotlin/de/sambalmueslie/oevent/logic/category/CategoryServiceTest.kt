package de.sambalmueslie.oevent.logic.category

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.category.api.CategoryChangeRequest
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class CategoryServiceTest {

	@Inject
	lateinit var service: CategoryService

	@Test
	fun `just run`() {
		assertNotNull(service)
	}

	@Test
	fun `create update get and delete a category`() {
		val name ="Test name"
		val url = "Test URL"
		val request = CategoryChangeRequest(name, url)
		val result = service.create(request)
		assertEquals(Category(1, name, url), result)

	}

}
