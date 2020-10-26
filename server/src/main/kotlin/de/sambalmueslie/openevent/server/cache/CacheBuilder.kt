package de.sambalmueslie.openevent.server.cache


import io.micronaut.caffeine.cache.Cache
import io.micronaut.caffeine.cache.Caffeine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.TimeUnit





internal class CacheBuilder {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(CacheBuilder::class.java)
	}

	fun <K, V> create(name: String, keyType: Class<K>, valueType: Class<V>, size: Long, ttl: Duration): Cache<K, V> {
		logger.info("Create new cache '$name' for $keyType and $valueType")
		return Caffeine.newBuilder()
				.maximumSize(size)
				.expireAfterWrite(ttl)
				.build()
	}
}
