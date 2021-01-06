package de.sambalmueslie.openevent.server.item.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemDescriptionChangeRequest(
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
) : BusinessObjectChangeRequest
