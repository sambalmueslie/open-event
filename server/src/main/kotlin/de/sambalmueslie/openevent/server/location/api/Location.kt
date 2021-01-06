package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("address")
	val address: Address,
	@JsonProperty("geoLocation")
	val geoLocation: GeoLocation,
	@JsonProperty("properties")
	val properties: LocationProperties
) : BusinessObject {

	fun format(): String {
		return address.format()
	}
}
