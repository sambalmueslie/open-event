package de.sambalmueslie.openevent.server.messaging.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageHeader
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.user.api.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Message")
@Table(name = "message")
data class MessageData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	// message header
	@Column(nullable = false)
	val authorId: Long = 0,
	@Column(nullable = false)
	val recipientId: Long = 0,
	@Column(nullable = false)
	val subject: String = "",
	@Column
	val parentMessageId: Long? = null,
	@Column
	val itemId: Long? = null,
	// message content
	@Column(columnDefinition = "TEXT", nullable = false)
	var content: String = "",
	// message status
	@Column
	@Enumerated(EnumType.STRING)
	var status: MessageStatus = MessageStatus.CREATED,
	@Column
	val createdTimestamp: LocalDateTime = LocalDateTime.now(),
	@Column
	var readTimestamp: LocalDateTime? = null,
	@Column
	var repliedTimestamp: LocalDateTime? = null,

	) : DataObject<Message, MessageConvertContent> {
	companion object {
		fun convert(user: User, request: MessageChangeRequest) =
			MessageData(
				authorId = user.id,
				recipientId = request.recipientId,
				subject = request.subject,
				content = request.content,
				parentMessageId = request.parentMessageId,
				itemId = request.itemId
			)

	}

	override fun convert(content: MessageConvertContent): Message {
		val header = MessageHeader(content.author, content.recipient, subject, parentMessageId, itemId)
		return Message(id, header, this.content, status, getStatusHistory())
	}

	private fun getStatusHistory(): Map<MessageStatus, LocalDateTime> {
		val result = mutableMapOf<MessageStatus, LocalDateTime>()
		result[MessageStatus.CREATED] = createdTimestamp
		readTimestamp?.let { result[MessageStatus.READ] = it }
		repliedTimestamp?.let { result[MessageStatus.REPLIED] = it }
		return result
	}

	fun markRead() {
		if (status != MessageStatus.CREATED) return

		status = MessageStatus.READ
		readTimestamp = LocalDateTime.now()
	}

	fun markReplied() {
		if (status == MessageStatus.REPLIED) return
		status = MessageStatus.REPLIED
		repliedTimestamp = LocalDateTime.now()
	}

}
