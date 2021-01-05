package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationChangeRequest(
	@JsonProperty("address")
	val address: Address = Address(),
	@JsonProperty("geoLocation")
	val geoLocation: GeoLocation,
	@JsonProperty("properties")
	val properties: LocationProperties = LocationProperties()
)
