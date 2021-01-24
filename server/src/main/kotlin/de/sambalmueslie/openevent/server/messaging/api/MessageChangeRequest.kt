package de.sambalmueslie.openevent.server.messaging.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageChangeRequest(
	@JsonProperty("subject")
	val subject: String,
	@JsonProperty("content")
	val content: String,
	@JsonProperty("recipientId")
	val recipientId: Long,
	@JsonProperty("parentMessageId")
	val parentMessageId: Long?,
	@JsonProperty("itemId")
	val itemId: Long?
) : BusinessObjectChangeRequest
