package de.sambalmueslie.openevent.server.cache


import io.micronaut.caffeine.cache.Cache
import io.micronaut.caffeine.cache.Policy
import io.micronaut.caffeine.cache.stats.CacheStats
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Function

class ConfigurableCache<K : Any, V : Any>(
		private var enabled: Boolean,
		private val name: String,
		private val cache: Cache<K, V>
) : Cache<K, V> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(ConfigurableCache::class.java)
	}

	internal fun setEnabled(enabled: Boolean) {
		logger.info("[$name] set enabled = $enabled")
		this.enabled = enabled
		if(enabled == false){
			cache.invalidateAll()
		}
	}

	override fun asMap(): ConcurrentMap<K, V> {
		if(enabled) return cache.asMap()
		return ConcurrentHashMap()
	}


	override fun cleanUp() {
		if(enabled) cache.cleanUp()
	}

	override fun estimatedSize(): Long {
		if(enabled) return cache.estimatedSize()
		return 0
	}

	override fun get(key: K, mappingFunction: Function<in K, out V>): V? {
		if(enabled) return cache.get(key, mappingFunction)
		return null
	}

	override fun getAllPresent(keys: MutableIterable<*>): MutableMap<K, V> {
		if(enabled) return cache.getAllPresent(keys)
		return mutableMapOf()
	}

	override fun getIfPresent(key: Any): V? {
		if(enabled) return cache.getIfPresent(key)
		return null
	}

	override fun invalidate(key: Any) {
		if(enabled) return cache.invalidate(key)
	}

	override fun invalidateAll() {
		if(enabled) cache.invalidateAll()
	}

	override fun invalidateAll(keys: MutableIterable<*>) {
		if(enabled) cache.invalidateAll(keys)
	}

	override fun policy(): Policy<K, V> {
		return cache.policy()
	}

	override fun put(key: K, value: V) {
		if(enabled) cache.put(key,value)
	}

	override fun putAll(map: MutableMap<out K, out V>) {
		if(enabled) cache.putAll(map)
	}

	override fun stats(): CacheStats {
		return cache.stats()
	}



}
