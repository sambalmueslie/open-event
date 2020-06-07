package de.sambalmueslie.oevent.logic.structure.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.logic.common.BusinessObjectChangeRequest
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.location.api.LocationChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class StructureChangeRequest(
		@JsonProperty("item")
		val item: ItemChangeRequest,
		@JsonProperty("location")
		val location: LocationChangeRequest?,
		@JsonProperty("parent")
		val parentStructureId: Long?
) : BusinessObjectChangeRequest
