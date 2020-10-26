package de.sambalmueslie.openevent.server.cache

import de.sambalmueslie.openevent.server.cache.api.CacheSettings
import de.sambalmueslie.openevent.server.common.AuthenticationHelper
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/cache")
class CacheController(
		private val authenticationHelper: AuthenticationHelper,
		private val service: CacheService
) {

	@Get
	fun getSettings(authentication: Authentication): List<CacheSettings> {
		authenticationHelper.checkForAdmin(authentication)
		return service.getAll()
	}

	@Get("/name/{name}")
	fun getSettings(authentication: Authentication, @PathVariable name: String): CacheSettings? {
		authenticationHelper.checkForAdmin(authentication)
		return service.getByName(name)
	}

	@Put("/name/{name}")
	fun enableSetting(authentication: Authentication, @PathVariable name: String): List<CacheSettings> {
		authenticationHelper.checkForAdmin(authentication)
		return service.enable(name)
	}

	@Delete("/name/{name}")
	fun disableSetting(authentication: Authentication, @PathVariable name: String): List<CacheSettings> {
		authenticationHelper.checkForAdmin(authentication)
		return service.disable(name)
	}

	@Delete("/name/{name}/clear")
	fun clearCache(authentication: Authentication, @PathVariable name: String): List<CacheSettings> {
		authenticationHelper.checkForAdmin(authentication)
		return service.clear(name)
	}
}
