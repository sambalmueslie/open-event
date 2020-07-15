package de.sambalmueslie.oevent.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class Conversation(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("author")
		val author: User,
		@JsonProperty("recipients")
		val recipients: List<User>,
		@JsonProperty("item")
		val item: Item?,
		@JsonProperty("messages")
		val messages: List<Message>
) : BusinessObject
