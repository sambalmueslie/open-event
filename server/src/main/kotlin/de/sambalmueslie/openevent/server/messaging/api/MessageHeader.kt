package de.sambalmueslie.openevent.server.messaging.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageHeader(
	@JsonProperty("author")
	val author: User,
	@JsonProperty("recipient")
	val recipient: User,
	@JsonProperty("subject")
	val subject: String,
	@JsonProperty("parentMessageId")
	val parentMessageId: Long?,
	@JsonProperty("itemId")
	val itemId: Long?
)
