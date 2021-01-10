package de.sambalmueslie.openevent.server.category.db

import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.query.builder.sql.Dialect
import kotlin.streams.toList

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class CategoryRepo(private val jdbcOperations: JdbcOperations) : CategoryRepository {
	override fun findExisting(request: CategoryChangeRequest): CategoryData? {
		val sql = "SELECT * FROM category AS c JOIN category_item_relation AS ci on c.id = ci.category_id WHERE c.name = ? and ci.item_id = ?"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setString(1, request.name)
			statement.setLong(2, request.itemId)
			val resultSet = statement.executeQuery()
			jdbcOperations.entityStream(resultSet, CategoryData::class.java).findFirst().orElseGet { null }
		}
	}

	override fun getAllForItem(itemId: Long): List<CategoryData> {
		val sql = "SELECT * FROM category AS c JOIN category_item_relation AS ci on c.id = ci.category_id WHERE ci.item_id = ?"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setLong(1, itemId)
			val resultSet = statement.executeQuery()
			jdbcOperations.entityStream(resultSet, CategoryData::class.java).toList()
		}
	}
}
