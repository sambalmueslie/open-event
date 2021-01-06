package de.sambalmueslie.openevent.server.cache.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject


@JsonIgnoreProperties(ignoreUnknown = true)
data class CacheSettings(
		@JsonProperty("id")
		override val id: Long,
		@JsonProperty("name")
		val name: String,
		@JsonProperty("enabled")
		val enabled: Boolean
) : BusinessObject
