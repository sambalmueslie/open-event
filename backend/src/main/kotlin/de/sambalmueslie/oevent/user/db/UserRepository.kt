package de.sambalmueslie.oevent.user.db

import io.micronaut.cache.annotation.Cacheable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository

@Repository
interface UserRepository : PageableRepository<UserData, Long> {
	@Cacheable("users")
	fun findByExternalId(externalId: String): UserData?

	@Cacheable("users")
	fun findByEmail(email: String): UserData?

	@Cacheable("users")
	fun findByUserName(username: String): UserData?
}
