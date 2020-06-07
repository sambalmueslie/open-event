package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationChangeRequest(
		@JsonProperty("address")
		val address: Address = Address(),
		@JsonProperty("geoLocation")
		val geoLocation: GeoLocation,
		@JsonProperty("size")
		val size: Int
) : BusinessObjectChangeRequest
