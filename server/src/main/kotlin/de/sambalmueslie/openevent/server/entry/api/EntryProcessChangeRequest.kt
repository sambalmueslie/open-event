package de.sambalmueslie.openevent.server.entry.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.item.api.ItemType

@JsonIgnoreProperties(ignoreUnknown = true)
data class EntryProcessChangeRequest(
	@JsonProperty("itemId")
	val itemId: Long,
	@JsonProperty("type")
	val type: ItemType,
	@JsonProperty("entitlement")
	val entitlement: Entitlement
) : BusinessObjectChangeRequest
