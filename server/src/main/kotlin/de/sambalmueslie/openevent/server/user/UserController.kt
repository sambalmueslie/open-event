package de.sambalmueslie.openevent.server.user

import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.api.UserChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/user")
class UserController(
		private val authenticationHelper: AuthenticationHelper,
		private val service: UserService
) {

	@Get
	fun user(authentication: Authentication): User? = service.getUser(authentication)

	@Get("/all")
	fun getAll(authentication: Authentication, pageable: Pageable): Page<User> {
		authenticationHelper.checkForAdmin(authentication)
		return service.getAll(authentication, pageable)
	}

	@Get("/filter/by/name")
	fun filterByName(authentication: Authentication, @QueryValue name: String, pageable: Pageable): Page<User> {
		authenticationHelper.checkForAdmin(authentication)
		return service.filterByName(name, pageable)
	}

	@Put("/{userId}")
	fun update(authentication: Authentication, @PathVariable userId: Long, @Body request: UserChangeRequest): User? {
		authenticationHelper.checkForAdmin(authentication)
		return service.update(userId, request)
	}

	@Post("/sync")
	fun syncIdp(authentication: Authentication) {
		authenticationHelper.checkForAdmin(authentication)
		service.syncWithIdp()
	}

	@Put("/sync")
	fun syncUser(authentication: Authentication): User? {
		authenticationHelper.checkForAdmin(authentication)
		return service.syncUser(authentication)
	}

	@Delete("/cache")
	fun clear(authentication: Authentication) {
		authenticationHelper.checkForAdmin(authentication)
		service.clearCache()
	}
}
