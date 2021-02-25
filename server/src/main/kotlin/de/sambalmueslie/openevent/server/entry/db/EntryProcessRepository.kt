package de.sambalmueslie.openevent.server.entry.db

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EntryProcessRepository : PageableRepository<EntryProcessData, Long> {
	fun findByItemId(itemId: Long, pageable: Pageable): Page<EntryProcessData>
	fun findByUserId(userId: Long, pageable: Pageable): Page<EntryProcessData>

	fun deleteByItemId(itemId: Long)
	fun deleteByItemIdAndUserId(itemId: Long, userId: Long)


	@Query(
			value = """
                SELECT p.*
                FROM entry_process AS p
                         JOIN item_entitlement_entry AS i ON i.item_id = p.item_id
                WHERE (i.user_id = :userId AND i.entitlement != 'NONE') OR p.user_id = :userId
			""",
			countQuery = """
			    SELECT COUNT(*)
                FROM entry_process AS p
                         JOIN item_entitlement_entry AS i ON i.item_id = p.item_id
                WHERE (i.user_id = :userId AND i.entitlement != 'NONE') OR p.user_id = :userId
			"""
	)
	fun getAllAccessible(userId: Long, pageable: Pageable): Page<EntryProcessData>

}
