package de.sambalmueslie.openevent.server.event.api

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Period(
        val start: LocalDateTime,
        val end: LocalDateTime
) {

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("dd. MMMM. YYYY", Locale.GERMAN)
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)
    }

    fun format(): String {
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()
        val startTime = start.toLocalTime()
        val endTime = end.toLocalTime()

        // 22. Okt. 2020 18:00 - 22:00 Uhr
        if (startDate == endDate) {
            return "${dateFormatter.format(startDate)} ${timeFormatter.format(startTime)} - ${timeFormatter.format(endTime)}"
        }
        return "${dateFormatter.format(startDate)} ${timeFormatter.format(startTime)} - " +
                "${dateFormatter.format(endDate)} ${timeFormatter.format(endTime)}"
    }
}
