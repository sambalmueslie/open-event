package de.sambalmueslie.oevent.logic.period.api

import de.sambalmueslie.oevent.logic.common.BusinessObjectChangeRequest
import java.time.LocalDateTime

data class PeriodChangeRequest(
		val start: LocalDateTime,
		val end: LocalDateTime
) : BusinessObjectChangeRequest
