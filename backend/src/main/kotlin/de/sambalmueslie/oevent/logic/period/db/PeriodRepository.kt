package de.sambalmueslie.oevent.logic.period.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface PeriodRepository : CrudRepository<PeriodData, Long> {

}
