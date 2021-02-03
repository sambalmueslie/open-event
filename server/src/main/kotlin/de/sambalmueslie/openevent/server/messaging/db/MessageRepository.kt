package de.sambalmueslie.openevent.server.messaging.db

import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
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
	fun findByAuthorIdOrRecipientId(authorId: Long, recipientId: Long, pageable: Pageable): Page<MessageData>

	fun countByRecipientIdAndStatus(recipientId: Long, status: MessageStatus): Int
	fun findByRecipientIdAndStatus(recipientId: Long, status: MessageStatus, pageable: Pageable): Page<MessageData>
}
