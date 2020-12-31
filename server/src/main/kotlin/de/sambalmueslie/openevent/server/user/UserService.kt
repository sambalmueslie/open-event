package de.sambalmueslie.openevent.server.user

import de.sambalmueslie.openevent.server.cache.CacheService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.api.UserChangeRequest
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.server.user.idp.IdpConnector
import de.sambalmueslie.openevent.server.user.update.UserUpdater
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.Principal
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton
import javax.transaction.Transactional
import kotlin.system.measureTimeMillis

@Singleton
@Secured(SecurityRule.IS_AUTHENTICATED)
open class UserService(
		cacheService: CacheService,
		private val repository: UserRepository,
		private val idpConnector: IdpConnector
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
	}

	private val externalIdCache = cacheService.getCache("UserExternalId", String::class.java, User::class.java)
	private val userCache = cacheService.getCache("UserId", Long::class.java, User::class.java)
	private val activeUpdater = ConcurrentHashMap<String, UserUpdater>()

	@Transactional
	open fun getAll(authentication: Authentication, pageable: Pageable) = repository.findAll(pageable).map { it.convert() }

	@Transactional
	open fun filterByName(name: String, pageable: Pageable): Page<User> {
		val searchString = "$name%"
		return repository.findByUserNameLikeOrFirstNameLikeOrLastNameLike(searchString, searchString, searchString, pageable).map { it.convert() }
	}

	@Transactional
	open fun update(userId: Long, request: UserChangeRequest): User? {
		val data = repository.findByIdOrNull(userId) ?: return null
		data.iconUrl = request.iconUrl
		val result = repository.save(data)
		externalIdCache.invalidate(data.externalId)
		userCache.invalidate(result.id)
		return result.convert()
	}

	@Transactional
	open fun getUser(authentication: Authentication): User? {
		val externalId = getUserExternalId(authentication) ?: return null
		val hit = externalIdCache.getIfPresent(externalId)
		if (hit != null) return hit
		return updateData(externalId)
	}


	@Scheduled(cron = "0 0 3 * * *")
	fun syncWithIdp() {
		val duration = measureTimeMillis {
			repository.findByServiceUserFalse().forEach { updateData(it.externalId) }
			userCache.invalidateAll()
			externalIdCache.invalidateAll()
		}
		logger.info("Sync idp user with idp finished after $duration ms.")
	}

	@Transactional
	open fun syncUser(authentication: Authentication): User? {
		val externalId = getUserExternalId(authentication) ?: return null
		val data = repository.findByExternalId(externalId) ?: return null
		return syncWithIdp(data)
	}

	private fun syncWithIdp(data: UserData): User? {
		val updater = activeUpdater.getOrPut(data.externalId)  { UserUpdater(idpConnector, repository, externalIdCache, userCache) }
		val result = updater.update(data.externalId) ?: return null
		externalIdCache.put(result.externalId, result)
		userCache.put(result.id, result)
		return result
	}

	private fun updateData(externalId: String): User? {
		val updater = activeUpdater.getOrPut(externalId) { UserUpdater(idpConnector, repository, externalIdCache, userCache) }
		return updater.update(externalId)
	}

	fun clearCache() {
		logger.info("Clear cache")
		externalIdCache.invalidateAll()
		userCache.invalidateAll()
	}

	private fun getUserExternalId(authentication: Authentication) = authentication.name
	private fun getUserExternalId(principal: Principal) = principal.name
}
