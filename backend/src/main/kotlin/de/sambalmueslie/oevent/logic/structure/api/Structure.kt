package de.sambalmueslie.oevent.logic.structure.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.oevent.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class Structure(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("root")
		val root: Boolean
) : BusinessObject
