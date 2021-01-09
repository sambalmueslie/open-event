package de.sambalmueslie.openevent.server.cache

import de.sambalmueslie.openevent.server.cache.api.CacheSettings
import de.sambalmueslie.openevent.server.cache.db.CacheSettingsData
import de.sambalmueslie.openevent.server.cache.db.CacheSettingsRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import javax.inject.Inject

@MicronautTest
internal class CacheServiceTest {

	@Inject
	lateinit var service: CacheService

	@Inject
	lateinit var repository: CacheSettingsRepository

	@Test
	fun `check test setup`() {
		assertNotNull(service)
		assertNotNull(repository)
	}

	@BeforeEach
	fun setup() {
		repository.deleteAll()
	}

	@Test
	fun `create cache and checkout service functions`() {
		val name = "test"
		val settingsId: Long = 3

		val cache = service.getCache(name, String::class.java, String::class.java, 100, Duration.ofMinutes(5))
		assertNotNull(cache)

		assertEquals(listOf(CacheSettingsData(settingsId, name, true)), repository.findAll())

		assertEquals(CacheSettings(settingsId, name, true), service.getByName(name))
		assertEquals(listOf(CacheSettings(settingsId, name, true)), service.getAll())

		cache.put("test", "value")
		assertEquals("value", cache.getIfPresent("test"))

		service.disable(name)
		assertNull(cache.getIfPresent("test"))
		assertEquals(listOf(CacheSettingsData(settingsId, name, false)), repository.findAll())
		cache.put("test", "value")
		assertNull(cache.getIfPresent("test"))

		service.enable(name)
		assertNull(cache.getIfPresent("test"))
		assertEquals(listOf(CacheSettingsData(settingsId, name, true)), repository.findAll())

	}

}
