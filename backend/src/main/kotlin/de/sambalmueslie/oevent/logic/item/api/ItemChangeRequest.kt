package de.sambalmueslie.oevent.logic.item.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
interface ItemChangeRequest {
	val title: String
	val shortText: String
	val longText: String
	val imageUrl: String
	val iconUrl: String
}
