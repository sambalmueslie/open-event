package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class EventRepo(private val jdbcOperations: JdbcOperations) : EventRepository {

	override fun findExisting(user: User, request: EventChangeRequest): EventData? {
		val sql = "SELECT * FROM event AS e JOIN item_description AS d on e.id = d.id WHERE d.title = ? and e.owner_id = ?"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setString(1, request.item.title)
			statement.setLong(2, user.id)
			val resultSet = statement.executeQuery()
			jdbcOperations.entityStream(resultSet, EventData::class.java).findFirst().orElseGet { null }
		}
	}

	@Query(
			value = """
                SELECT e.*
                FROM event AS e
                         JOIN item_entitlement_entry AS i ON i.item_id = e.id
                WHERE i.user_id = :userId AND i.entitlement != 'NONE'
			""",
			countQuery = """
			    SELECT COUNT(*)
                FROM event AS e
                         JOIN item_entitlement_entry AS i ON i.item_id = e.id
                WHERE i.user_id = :userId AND i.entitlement != 'NONE'
			"""
	)
	abstract override fun getAllAccessible(userId: Long, pageable: Pageable): Page<EventData>
}
