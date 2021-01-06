package de.sambalmueslie.openevent.server.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.item.api.Item
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.user.api.User

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("period")
	val period: Period,
	@JsonProperty("owner")
	override val owner: User,
	@JsonProperty("description")
	override val description: ItemDescription,
	@JsonProperty("location")
	val location: Location?
) : BusinessObject, Item
