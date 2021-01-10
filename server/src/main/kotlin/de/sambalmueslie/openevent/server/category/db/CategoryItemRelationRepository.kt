package de.sambalmueslie.openevent.server.category.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.jdbc.runtime.JdbcOperations
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
abstract class CategoryItemRelationRepository(private val jdbcOperations: JdbcOperations) : GenericRepository<CategoryItemRelationData, Long> {
	abstract fun findByCategoryIdAndItemId(categoryId: Long, itemId: Long): CategoryItemRelationData?
	abstract fun findByItemId(itemId: Long): List<CategoryItemRelationData>
	abstract fun findByCategoryId(categoryId: Long): List<CategoryItemRelationData>

	fun saveAll(relations: List<CategoryItemRelationData>) {
		val sql = "INSERT INTO category_item_relation(category_id, item_id) VALUES (?,?)"
		batchExecution(sql, relations)
	}

	fun deleteAll(relations: List<CategoryItemRelationData>) {
		val sql = "DELETE FROM category_item_relation WHERE category_id = ? AND item_id = ?"
		batchExecution(sql, relations)
	}

	private fun batchExecution(sql: String, relations: List<CategoryItemRelationData>) {
		return jdbcOperations.prepareStatement(sql) { statement ->
			relations.chunked(100).forEach {
				it.forEach {
					statement.setLong(1, it.categoryId)
					statement.setLong(2, it.itemId)
					statement.addBatch()
				}
				statement.executeBatch()
			}
		}
	}

	fun save(relation: CategoryItemRelationData): CategoryItemRelationData {
		val sql = "INSERT INTO category_item_relation(category_id, item_id) VALUES (?,?)"
		return jdbcOperations.prepareStatement(sql) { statement ->
			statement.setLong(1, relation.categoryId)
			statement.setLong(2, relation.itemId)
			statement.execute()
			relation
		}
	}

	fun delete(relation: CategoryItemRelationData) {
		val sql = "DELETE FROM category_item_relation WHERE category_id = ? AND item_id = ?"
		jdbcOperations.prepareStatement(sql) { statement ->
			statement.setLong(1, relation.categoryId)
			statement.setLong(2, relation.itemId)
			statement.execute()
		}
	}
}
