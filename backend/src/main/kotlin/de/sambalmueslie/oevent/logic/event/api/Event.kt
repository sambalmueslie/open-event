package de.sambalmueslie.oevent.logic.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("period")
		val period: Period,
		@JsonProperty("parent")
		val parent: Event?,
		@JsonProperty("hasLocation")
		val hasLocation: Boolean,
		@JsonProperty("hasRegistration")
		val hasRegistration: Boolean
) : BusinessObject
