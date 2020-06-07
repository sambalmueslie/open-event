package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("address")
		val address: Address,
		@JsonProperty("geoLocation")
		val geoLocation: GeoLocation,
		@JsonProperty("size")
		val size: Int = -1
) : BusinessObject
