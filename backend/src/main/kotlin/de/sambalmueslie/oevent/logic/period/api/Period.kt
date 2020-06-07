package de.sambalmueslie.oevent.logic.period.api

import de.sambalmueslie.oevent.logic.common.BusinessObject
import java.time.LocalDateTime

data class Period(
		override val id: Long,
		val start: LocalDateTime,
		val end: LocalDateTime
) : BusinessObject
