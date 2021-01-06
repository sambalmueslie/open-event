package de.sambalmueslie.oevent.user.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserChangeRequest(
		@JsonProperty("externalId")
		val externalId: String = "",
		@JsonProperty("userName")
		val userName: String = "",
		@JsonProperty("firstName")
		val firstName: String = "",
		@JsonProperty("lastName")
		val lastName: String = "",
		@JsonProperty("email")
		val email: String = "",
		@JsonProperty("iconUrl")
		val iconUrl: String = "",
		@JsonProperty("type")
		val type: UserType = UserType.IDP
): BusinessObjectChangeRequest
