package de.sambalmueslie.openevent.server.structure.db


import de.sambalmueslie.openevent.server.event.db.EventData
import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.query.builder.sql.Dialect
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class StructureRepo(private val jdbcOperations: JdbcOperations) : StructureRepository {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(StructureRepo::class.java)
	}

	override fun findExisting(user: User, request: StructureChangeRequest): StructureData? {
		val sql = "SELECT * FROM structure AS s JOIN item_description AS d on s.id = d.id " +
				"WHERE d.title = ? and s.owner_id = ?"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setString(1, request.item.title)
			statement.setLong(2, user.id)
			val resultSet = statement.executeQuery()
			jdbcOperations.entityStream(resultSet, StructureData::class.java).filter { it.parentStructureId == request.parentStructureId }.findFirst().orElseGet { null }
		}
	}
}
