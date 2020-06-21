package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Message
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.*

@Entity(name = "Message")
@Table(name = "message")
data class MessageData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false)
		val subject: String = "",
		@Column(nullable = false, columnDefinition = "TEXT")
		val content: String = "",
		@Column(nullable = false)
		val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
		@Column(nullable = false)
		var read: Boolean = false,
		@Column(nullable = false)
		var replied: Boolean = false
) : DataObject<Message> {

	override fun convert(context: DataObjectContext): Message {
		return Message(id, subject, content, timestamp, read, replied)
	}

	@ManyToOne(fetch = FetchType.LAZY)
	var conversation: ConversationData? = null

}
