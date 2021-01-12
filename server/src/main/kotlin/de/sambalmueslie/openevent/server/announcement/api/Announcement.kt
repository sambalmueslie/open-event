package de.sambalmueslie.openevent.server.announcement.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.user.api.User
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Announcement(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("subject")
	val subject: String,
	@JsonProperty("content")
	val content: String,
	@JsonProperty("author")
	val author: User,
	@JsonProperty("timestamp")
	val timestamp: LocalDateTime,
	@JsonProperty("itemId")
	val itemId: Long
) : BusinessObject
