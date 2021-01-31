package de.sambalmueslie.openevent.server.announcement.db

import de.sambalmueslie.openevent.server.structure.db.StructureData
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface AnnouncementRepository : PageableRepository<AnnouncementData, Long> {
	fun findByItemId(itemId: Long, pageable: Pageable): Page<AnnouncementData>

	@Query(
		value = """
			SELECT a.*
			FROM Announcement a
         	JOIN ItemEntitlementEntry i ON i.itemId = a.id
			WHERE i.userId = :userId AND i.entitlement != 'NONE'
			""",
		countQuery = """
			SELECT COUNT(*)
			FROM Announcement a
         	JOIN ItemEntitlementEntry i ON i.itemId = a.id
			WHERE i.userId = :userId AND i.entitlement != 'NONE'
			"""
	)
	fun getAllAccessible(userId: Long, pageable: Pageable): Page<AnnouncementData>
}
