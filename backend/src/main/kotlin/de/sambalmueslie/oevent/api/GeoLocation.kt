package de.sambalmueslie.oevent.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocation(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("lat")
		val lat: Double,
		@JsonProperty("lon")
		val lon: Double
) : BusinessObject