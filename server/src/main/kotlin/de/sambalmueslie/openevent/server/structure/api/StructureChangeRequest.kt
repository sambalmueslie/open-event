package de.sambalmueslie.openevent.server.structure.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class StructureChangeRequest(
	@JsonProperty("item")
	override val item: ItemDescriptionChangeRequest,
	@JsonProperty("location")
	val location: LocationChangeRequest?,
	@JsonProperty("parent")
	val parentStructureId: Long?,
	@JsonProperty("autoAcceptViewer")
	val autoAcceptViewer: Boolean
) : ItemChangeRequest
