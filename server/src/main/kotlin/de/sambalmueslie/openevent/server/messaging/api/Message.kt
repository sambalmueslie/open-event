package de.sambalmueslie.openevent.server.messaging.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
	@JsonProperty("id")
	override val id: Long,
	val header: MessageHeader,
	@JsonProperty("content")
	val content: String,
	@JsonProperty("status")
	val status: MessageStatus,
	@JsonProperty("status")
	val statusHistory: Map<MessageStatus, LocalDateTime>
) : BusinessObject
