package de.sambalmueslie.oevent.logic.item.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.logic.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemChangeRequest(
		@JsonProperty("title")
		val title: String = "",
		@JsonProperty("shortText")
		val shortText: String = "",
		@JsonProperty("longText")
		val longText: String = "",
		@JsonProperty("imageUrl")
		val imageUrl: String = "",
		@JsonProperty("iconUrl")
		val iconUrl: String = ""
): BusinessObjectChangeRequest
