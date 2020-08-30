package de.sambalmueslie.oevent.logic.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventChangeRequest(
		@JsonProperty("item")
		val item: ItemChangeRequest,
		@JsonProperty("start")
		var start: LocalDateTime,
		@JsonProperty("stop")
		var stop: LocalDateTime
): BusinessObjectChangeRequest
