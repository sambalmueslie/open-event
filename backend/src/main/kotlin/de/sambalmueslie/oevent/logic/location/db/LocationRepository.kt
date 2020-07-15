package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.model.LocationData
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository

@Repository
interface LocationRepository : PageableRepository<LocationEntity, Long>
