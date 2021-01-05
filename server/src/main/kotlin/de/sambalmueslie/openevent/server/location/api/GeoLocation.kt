package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocation(
		@JsonProperty("lat")
		val lat: Double = 0.0,
		@JsonProperty("lon")
		val lon: Double = 0.0
)
