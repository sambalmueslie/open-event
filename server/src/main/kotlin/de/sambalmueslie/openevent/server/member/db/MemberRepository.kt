package de.sambalmueslie.openevent.server.member.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface MemberRepository : PageableRepository<MemberData, Long> {
	fun findByUserId(userId: Long, pageable: Pageable): Page<MemberData>
	fun findByItemId(itemId: Long, pageable: Pageable): Page<MemberData>
	fun findByUserIdAndItemId(userId: Long, itemId: Long): List<MemberData>
}
