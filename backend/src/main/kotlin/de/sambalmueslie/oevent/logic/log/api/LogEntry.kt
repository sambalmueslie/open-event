package de.sambalmueslie.oevent.logic.log.api


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.ZoneOffset

@JsonIgnoreProperties(ignoreUnknown = true)
data class LogEntry(
		@JsonProperty("id")
		var id: Long = 0,
		@JsonProperty("reference")
		val reference: String = "",
		@JsonProperty("userId")
		val userId: Long = 0,
		@JsonProperty("title")
		val title: String = "",
		@JsonProperty("description")
		val description: String = "",
		@JsonProperty("timestamp")
		val timestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)
)
