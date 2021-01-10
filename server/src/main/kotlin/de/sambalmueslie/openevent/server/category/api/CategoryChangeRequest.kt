package de.sambalmueslie.openevent.server.category.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryChangeRequest(
	@JsonProperty("name")
	val name: String,
	@JsonProperty("iconUrl")
	val iconUrl: String,
	@JsonProperty("itemId")
	val itemId: Long
) : BusinessObjectChangeRequest
