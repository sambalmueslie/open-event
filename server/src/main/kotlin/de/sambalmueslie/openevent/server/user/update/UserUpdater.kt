package de.sambalmueslie.openevent.server.user.update

import de.sambalmueslie.openevent.server.common.executeWithReturn
import de.sambalmueslie.openevent.server.common.measureTimeMillisWithReturn
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.server.user.idp.IdpConnector
import io.micronaut.caffeine.cache.Cache
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.Semaphore

class UserUpdater(
		private val idpConnector: IdpConnector,
		private val repository: UserRepository,
		private val externalCache: Cache<String, User>,
		private val cache: Cache<Long, User>
)  {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserUpdater::class.java)
	}

	private val semaphore = Semaphore(1)

	fun update(externalId: String): User? {
//		logger.debug("[$externalId] Update user - start")
		val (duration, result) = measureTimeMillisWithReturn {
			try {
				logger.isTraceEnabled.let { logger.trace("[$externalId] Semaphore acquire") }
				semaphore.acquire()
				updateUser(externalId)
			} catch (e: Exception) {
				logger.error("[$externalId] Update user - fail", e)
				null
			} finally {
				semaphore.release()
				logger.isTraceEnabled.let { logger.trace("[$externalId] Semaphore release") }
			}
		}
		logger.debug("[$externalId] Update user - $externalId finished within $duration ms")
		return result
	}

	fun update(data: UserData): User? {
		logger.debug("[${data.externalId}] sync user - start")
		val (duration, result) = measureTimeMillisWithReturn {
			try {
				logger.isTraceEnabled.let { logger.trace("[${data.externalId}] Semaphore acquire") }
				semaphore.acquire()
				syncWithIdp(data)
			} catch (e: Exception) {
				logger.error("[${data.externalId}] Update user - fail", e)
				null
			} finally {
				semaphore.release()
				logger.isTraceEnabled.let { logger.trace("[${data.externalId}] Semaphore release") }
			}
		}
		logger.debug("[${data.externalId}] Update user - ${data.externalId} finished within $duration ms")
		return result
	}

	private fun updateUser(externalId: String): User? {
		val data = repository.findByExternalId(externalId)
		return if (data != null) checkExisting(data) else syncWithIdp(externalId)
	}

	private fun checkExisting(data: UserData): User? {
//		logger.debug("[${data.externalId}] Update user - check existing")
		if (isSyncRequired(data)) {
			val result = syncWithIdp(data) ?: return null
			externalCache.put(result.externalId, result)
			cache.put(result.id, result)
			return result
		}
		val result = data.convert()
		externalCache.put(result.externalId, result)
		cache.put(result.id, result)
		return result
	}


	private fun syncWithIdp(externalId: String): User? {
		logger.debug("[$externalId] Update user - sync with idp")
		val idpValue = idpConnector.read(externalId) ?: return executeWithReturn { logger.error("Cannot find idp user for $externalId") }
		val existing = repository.findByExternalId(idpValue.externalId)

		val value = existing?.let { sync(idpValue, it) } ?: repository.save(idpValue)
		logger.isTraceEnabled.let { logger.trace("[$externalId] Update user - store data $value") }

		val result = repository.update(value).convert()
		externalCache.put(result.externalId, result)
		cache.put(result.id, result)
		return result
	}


	private fun isSyncRequired(data: UserData): Boolean {
		if (data.serviceUser) return false
		return data.lastSync.isBefore(LocalDateTime.now(ZoneOffset.UTC).minusDays(1))
	}


	private fun sync(idpValue: UserData, existing: UserData): UserData {
		logger.debug("[${existing.externalId}] Update user - sync with representation")
		existing.userName = idpValue.userName
		existing.firstName = idpValue.firstName
		existing.lastName = idpValue.lastName
		existing.email = idpValue.email
		existing.lastSync = LocalDateTime.now(ZoneOffset.UTC)
		return existing
	}



	private fun syncWithIdp(data: UserData): User? {
		val idpValue = idpConnector.read(data.externalId) ?: return executeWithReturn { logger.error("Cannot find idp user for ${data.externalId}") }
		return repository.update(sync(idpValue, data)).convert()
	}
}
