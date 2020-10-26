package de.sambalmueslie.openevent.server.cache

import de.sambalmueslie.openevent.server.cache.api.CacheSettings
import de.sambalmueslie.openevent.server.cache.db.CacheSettingsData
import de.sambalmueslie.openevent.server.cache.db.CacheSettingsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import javax.inject.Singleton


@Singleton
class CacheService(private val repository: CacheSettingsRepository) {
	companion object {
		val logger: Logger = LoggerFactory.getLogger(CacheService::class.java)
	}

	private val builder = CacheBuilder()
	private val caches = mutableMapOf<String, ConfigurableCache<*, *>>()

	fun <K, V> getCache(name: String, keyType: Class<K>, valueType: Class<V>, size: Long = 1000, ttl: Duration = Duration.ofMinutes(10)): ConfigurableCache<K, V> {
		if (ttl.isZero) logger.warn("Try to setup a cache '$name' with TTL of $ttl results in non working cache")
		val cache = builder.create(name, keyType, valueType, size, ttl)
		val config = repository.findByName(name) ?: repository.save(CacheSettingsData(0, name, true))
		val value = ConfigurableCache(config.enabled, name, cache)
		caches[name] = value
		return value
	}

	fun getByName(name: String) = repository.findByName(name)?.convert()
	fun getAll() = repository.findAll().sortedBy { it.id }.map { it.convert() }

	fun enable(name: String) = update(name, true)
	fun disable(name: String) = update(name, false)

	private fun update(name: String, enabled: Boolean): List<CacheSettings> {
		val config = repository.findByName(name) ?: return getAll()
		config.enabled = enabled
		repository.update(config)
		val cache = caches[name] ?: return getAll()
		cache.setEnabled(enabled)
		return getAll()
	}

	fun clear(name: String): List<CacheSettings> {
		caches[name]?.invalidateAll()
		return getAll()
	}
}
