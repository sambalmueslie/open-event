package de.sambalmueslie.oevent.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.logic.item.api.Item
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("subject")
		val subject: String,
		@JsonProperty("content")
		val content: String,
		@JsonProperty("timestamp")
		val timestamp: LocalDateTime,
		@JsonProperty("read")
		val read: Boolean,
		@JsonProperty("replied")
		val replied: Boolean
) : BusinessObject
