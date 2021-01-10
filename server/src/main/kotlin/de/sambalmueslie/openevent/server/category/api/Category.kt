package de.sambalmueslie.openevent.server.category.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("name")
	val name: String,
	@JsonProperty("iconUrl")
	val iconUrl: String
) : BusinessObject
