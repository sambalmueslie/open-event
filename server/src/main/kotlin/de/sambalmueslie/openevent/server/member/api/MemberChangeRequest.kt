package de.sambalmueslie.openevent.server.member.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement

@JsonIgnoreProperties(ignoreUnknown = true)
data class MemberChangeRequest(
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("entitlement")
	val entitlement: Entitlement = Entitlement.VIEWER,
	@JsonProperty("contact")
	val contact: Boolean = false
) : BusinessObjectChangeRequest
