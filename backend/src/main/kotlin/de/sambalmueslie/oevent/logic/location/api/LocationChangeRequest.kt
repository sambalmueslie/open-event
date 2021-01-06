package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationChangeRequest(
		@JsonProperty("address")
		val address: AddressChangeRequest = AddressChangeRequest(),
		@JsonProperty("geoLocation")
		val geoLocation: GeoLocationChangeRequest = GeoLocationChangeRequest(),
		@JsonProperty("size")
		val size: Int = 0
) : BusinessObjectChangeRequest
