package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddressChangeRequest(
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
) : BusinessObjectChangeRequest
