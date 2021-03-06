package de.sambalmueslie.openevent.server.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventChangeRequest(
	@JsonProperty("item")
	override val item: ItemDescriptionChangeRequest,
	@JsonProperty("period")
	val period: PeriodChangeRequest,
	@JsonProperty("location")
	val location: LocationChangeRequest?,
) : ItemChangeRequest
