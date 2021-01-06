package de.sambalmueslie.oevent.logic.location.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository

@Repository
interface GeoLocationRepository : PageableRepository<GeoLocationEntity, Long> {
}
