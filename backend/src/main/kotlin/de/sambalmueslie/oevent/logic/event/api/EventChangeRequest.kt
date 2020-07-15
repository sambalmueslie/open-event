package de.sambalmueslie.oevent.logic.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventChangeRequest(
		@JsonProperty("start")
		var start: LocalDateTime,
		@JsonProperty("stop")
		var stop: LocalDateTime,
		@JsonProperty("title")
		override val title: String,
		@JsonProperty("shortText")
		override val shortText: String,
		@JsonProperty("longText")
		override val longText: String,
		@JsonProperty("imageUrl")
		override val imageUrl: String,
		@JsonProperty("iconUrl")
		override val iconUrl: String
) : ItemChangeRequest
