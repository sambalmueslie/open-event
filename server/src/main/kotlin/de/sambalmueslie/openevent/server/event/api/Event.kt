package de.sambalmueslie.openevent.server.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.item.api.Item
import de.sambalmueslie.openevent.server.item.api.ItemDescription

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("period")
	val period: Period,
	@JsonProperty("userId")
	override val userId: Long,
	@JsonProperty("description")
	override val description: ItemDescription
) : BusinessObject, Item
