package de.sambalmueslie.oevent.logic.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Address(
		@JsonProperty("street")
		val street: String = "",
		@JsonProperty("streetNumber")
		val streetNumber: String = "",
		@JsonProperty("zip")
		val zip: String = "",
		@JsonProperty("city")
		val city: String = "",
		@JsonProperty("country")
		val country: String = "",
		@JsonProperty("additionalInfo")
		val additionalInfo: String = ""
)
