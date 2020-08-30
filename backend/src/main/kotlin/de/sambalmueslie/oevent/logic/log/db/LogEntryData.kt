package de.sambalmueslie.oevent.logic.log.db

import de.sambalmueslie.oevent.logic.log.api.LogEntry
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.*

@Entity(name = "LogEntry")
@Table(name = "log_entry")
data class LogEntryData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		var id: Long = 0,
		@Column(nullable = false)
		val reference: String = "",
		@Column(nullable = false)
		val userId: Long = 0,
		@Column(columnDefinition = "TEXT")
		val title: String = "",
		@Column(columnDefinition = "TEXT")
		val description: String = "",
		@Column(nullable = false)
		val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
) {
	fun convert() = LogEntry(id, reference, userId, title, description, timestamp)
}
