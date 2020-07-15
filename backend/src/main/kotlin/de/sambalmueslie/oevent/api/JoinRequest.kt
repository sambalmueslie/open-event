package de.sambalmueslie.oevent.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class JoinRequest(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("user")
		val user: User,
		@JsonProperty("structure")
		val structure: Structure,
		@JsonIgnoreProperties("status")
		val status: JoinStatus,
		@JsonProperty("entitlement")
		val entitlement: Entitlement
) : BusinessObject
