package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocationChangeRequest(
	@JsonProperty("lat")
	val lat: Double = 0.0,
	@JsonProperty("lon")
	val lon: Double = 0.0
) : BusinessObjectChangeRequest
