package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.logic.event.api.Period
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class PeriodData(
		@Column
		var start: LocalDateTime,
		@Column(name = "_end")
		var end: LocalDateTime
) {

	fun convert(): Period {
		return Period(start, end)
	}
}
