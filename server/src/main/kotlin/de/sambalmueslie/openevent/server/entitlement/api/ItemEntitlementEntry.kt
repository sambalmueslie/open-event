package de.sambalmueslie.openevent.server.entitlement.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class ItemEntitlementEntry(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("userId")
	val userId: Long,
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("type")
	val type: ItemType,
	@JsonProperty("entitlement")
	val entitlement: Entitlement
) : BusinessObject
