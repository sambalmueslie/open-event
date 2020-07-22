package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.item.relation.EntityRepository
import de.sambalmueslie.oevent.logic.location.api.Location
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.PageableRepository

@Repository
interface LocationRepository : PageableRepository<LocationEntity, Long>, EntityRepository<Location, LocationEntity> {
	@Query(value = "SELECT l FROM Location l, LocationItemRelation r WHERE r.locationId = l.id")
	override fun findByItem(itemId: Long): List<LocationEntity>
}
