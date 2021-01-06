package de.sambalmueslie.oevent.logic.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.logic.item.api.Item
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.Column

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Event(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("title")
		override val title: String,
		@JsonProperty("shortText")
		override val shortText: String,
		@JsonProperty("longText")
		override val longText: String,
		@JsonProperty("imageUrl")
		override val imageUrl: String,
		@JsonProperty("iconUrl")
		override val iconUrl: String,
		@JsonProperty("start")
		var start: LocalDateTime,
		@JsonProperty("end")
		var end: LocalDateTime
) : Item, BusinessObject
