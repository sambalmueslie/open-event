package de.sambalmueslie.openevent.server.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationProperties(
	@JsonProperty("id")
	override val id: Long,
	@JsonProperty("size")
	val size: Int = -1
) : BusinessObject
