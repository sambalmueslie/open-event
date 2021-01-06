package de.sambalmueslie.openevent.server.event.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class PeriodChangeRequest(
	@JsonProperty("start")
	val start: LocalDateTime,
	@JsonProperty("end")
	val end: LocalDateTime
) {}
