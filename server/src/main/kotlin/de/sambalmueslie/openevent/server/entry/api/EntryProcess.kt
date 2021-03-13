package de.sambalmueslie.openevent.server.entry.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class EntryProcess(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("user")
	val user: User,
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("type")
	val type: ItemType,
	@JsonProperty("entitlement")
	val entitlement: Entitlement,
	@JsonIgnoreProperties("status")
	val status: EntryProcessStatus
) : BusinessObject
