package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.logic.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocation(
		@JsonProperty("lat")
		val lat: Double = 0.0,
		@JsonProperty("lon")
		val lon: Double = 0.0
)
