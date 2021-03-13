package de.sambalmueslie.openevent.server.member.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class Member(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("user")
	val user: User,
	@JsonProperty("entitlement")
	val entitlement: Entitlement,
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("contact")
	val contact: Boolean
) : BusinessObject
