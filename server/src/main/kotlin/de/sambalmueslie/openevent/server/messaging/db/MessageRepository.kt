package de.sambalmueslie.openevent.server.messaging.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface MessageRepository : PageableRepository<MessageData, Long> {
	fun findByItemId(itemId: Long, pageable: Pageable): Page<MessageData>
	fun findByAuthorId(authorId: Long, pageable: Pageable): Page<MessageData>
	fun findByRecipientId(recipientId: Long, pageable: Pageable): Page<MessageData>
}
