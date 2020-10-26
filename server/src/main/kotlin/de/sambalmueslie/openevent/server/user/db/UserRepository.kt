package de.sambalmueslie.openevent.server.user.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface UserRepository : PageableRepository<UserData, Long> {
	fun findByExternalId(externalId: String): UserData?
	fun findByUserName(username: String): List<UserData>
	fun findByServiceUserFalse(): List<UserData>
	fun findByUserNameLike(username: String, pageable: Pageable): Page<UserData>
	fun findByFirstNameLike(firstName: String, pageable: Pageable): Page<UserData>
	fun findByLastNameLike(lastName: String, pageable: Pageable): Page<UserData>
	fun findByUserNameLikeOrFirstNameLikeOrLastNameLike(username: String, firstName: String, lastName: String, pageable: Pageable): Page<UserData>
}
