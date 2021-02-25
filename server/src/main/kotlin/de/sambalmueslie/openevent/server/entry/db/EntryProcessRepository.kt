package de.sambalmueslie.openevent.server.entry.db

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



	fun getAllAccessible(userId: Long, pageable: Pageable): Page<EntryProcessData>

}
