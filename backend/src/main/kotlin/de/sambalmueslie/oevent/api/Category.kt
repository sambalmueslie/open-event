package de.sambalmueslie.oevent.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("name")
		val name: String,
		@JsonProperty("iconUrl")
		val iconUrl: String
) : BusinessObject
