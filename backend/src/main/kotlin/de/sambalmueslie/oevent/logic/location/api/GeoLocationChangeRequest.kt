package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocationChangeRequest(
		@JsonProperty("lat")
		val lat: Double = 0.0,
		@JsonProperty("lon")
		val lon: Double = 0.0
) : BusinessObjectChangeRequest
