package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class EventRepo(private val jdbcOperations: JdbcOperations) : EventRepository {

	override fun findExisting(user: User, request: EventChangeRequest): EventData? {
		val sql = "SELECT * FROM event AS e JOIN item_description AS d on e.id = d.id WHERE d.title = ? && e.user_id = ?"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setString(1, request.item.title)
			statement.setLong(2, user.id)
			val resultSet = statement.executeQuery()
			jdbcOperations.entityStream(resultSet, EventData::class.java).findFirst().orElseGet { null }
		}
	}
}
