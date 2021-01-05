package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationProperties(
		@JsonProperty("size")
		val size: Int = -1
)
