package de.sambalmueslie.openevent.server.announcement.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class AnnouncementChangeRequest(
	@JsonProperty("subject")
	val subject: String,
	@JsonProperty("content")
	val content: String,
	@JsonProperty("itemId")
	val itemId: Long,
) : BusinessObjectChangeRequest
