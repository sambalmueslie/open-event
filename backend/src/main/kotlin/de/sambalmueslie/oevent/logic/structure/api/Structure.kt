package de.sambalmueslie.oevent.logic.structure.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.logic.item.api.Item

@JsonIgnoreProperties(ignoreUnknown = true)
data class Structure(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("root")
		val root: Boolean,
		@JsonProperty("title")
		override val title: String,
		@JsonProperty("shortText")
		override val shortText: String,
		@JsonProperty("longText")
		override val longText: String,
		@JsonProperty("imageUrl")
		override val imageUrl: String,
		@JsonProperty("iconUrl")
		override val iconUrl: String
) : BusinessObject, Item
