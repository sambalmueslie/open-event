package de.sambalmueslie.openevent.server.structure.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.item.api.Item
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class Structure(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("root")
	val root: Boolean,
	@JsonProperty("visible")
	val visible: Boolean,
	@JsonProperty("autoAcceptViewer")
	val autoAcceptViewer: Boolean,
	@JsonProperty("public")
	val public: Boolean,
	@JsonProperty("owner")
	override val owner: User,
	@JsonProperty("description")
	override val description: ItemDescription,
	@JsonProperty("location")
	val location: Location?,
	@JsonProperty("type")
	override val type: ItemType = ItemType.STRUCTURE
) : BusinessObject, Item
