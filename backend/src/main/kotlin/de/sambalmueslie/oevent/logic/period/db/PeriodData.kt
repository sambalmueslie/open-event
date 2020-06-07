package de.sambalmueslie.oevent.logic.period.db

import de.sambalmueslie.oevent.logic.common.DataObject
import de.sambalmueslie.oevent.logic.period.api.Period
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Period")
@Table(name = "period")
data class PeriodData(
		@Id
		val id: Long,
		@Column
		var start: LocalDateTime,
		@Column(name = "_end")
		var end: LocalDateTime
) : DataObject<Period> {

	override fun convert(): Period {
		return Period(id, start, end)
	}
}
