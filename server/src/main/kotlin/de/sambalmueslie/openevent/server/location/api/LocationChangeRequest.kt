package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationChangeRequest(
	@JsonProperty("address")
	val address: AddressChangeRequest = AddressChangeRequest(),
	@JsonProperty("geoLocation")
	val geoLocation: GeoLocationChangeRequest = GeoLocationChangeRequest(),
	@JsonProperty("properties")
	val properties: LocationPropertiesChangeRequest = LocationPropertiesChangeRequest()
)
