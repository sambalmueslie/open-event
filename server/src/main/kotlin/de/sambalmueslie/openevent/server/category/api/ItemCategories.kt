package de.sambalmueslie.openevent.server.category.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemCategories(
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("categories")
	val categories: Set<Category>
)
