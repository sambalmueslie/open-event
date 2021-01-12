package de.sambalmueslie.openevent.server.announcement.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface AnnouncementRepository : PageableRepository<AnnouncementData, Long> {
	fun findByItemId(itemId: Long, pageable: Pageable): Page<AnnouncementData>
}
