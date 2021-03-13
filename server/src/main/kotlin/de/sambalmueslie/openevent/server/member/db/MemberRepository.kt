package de.sambalmueslie.openevent.server.member.db

import io.micronaut.data.annotation.Query
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

	@Query(
			value = """
                SELECT m.*
                FROM member AS m
                         JOIN item_entitlement_entry AS i ON i.item_id = m.item_id
                WHERE (i.user_id = :userId AND i.entitlement != 'NONE') OR m.user_id = :userId
			""",
			countQuery = """
			    SELECT COUNT(*)
                FROM member AS m
                         JOIN item_entitlement_entry AS i ON i.item_id = m.item_id
                WHERE (i.user_id = :userId AND i.entitlement != 'NONE') OR m.user_id = :userId
			"""
	)
    fun getAllAccessible(userId: Long, pageable: Pageable): Page<MemberData>
}
