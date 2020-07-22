package de.sambalmueslie.oevent.logic.location


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity
import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationMgr
import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelationService
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.logic.location.api.LocationChangeRequest
import de.sambalmueslie.oevent.logic.location.db.LocationEntity
import de.sambalmueslie.oevent.logic.location.db.LocationItemRelation
import de.sambalmueslie.oevent.logic.location.db.LocationItemRelationRepository
import de.sambalmueslie.oevent.logic.location.db.LocationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LocationService(
		repository: LocationRepository,
		relationRepository: LocationItemRelationRepository
) : BaseService<Location, LocationChangeRequest, LocationEntity>(repository, logger), ItemEntityRelationService<Location> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(LocationService::class.java)
	}

	private val factory = { id: String, i: Item, e: Location -> LocationItemRelation(id, e.id, i.id) }
	private val relationService = ItemEntityRelationMgr(repository, relationRepository, Location::class.java, factory)


	private val validator = LocationValidator()
	override fun getValidator() = validator

	private val merger = LocationMerger()
	override fun getMerger() = merger

	override fun add(item: Item, entity: Location): List<Location> = relationService.add(item, entity)
	override fun remove(item: Item, entity: Location): List<Location> = relationService.remove(item, entity)
	override fun get(item: Item): List<Location> = relationService.get(item)
	override fun getByItemId(itemId: Long): List<Location> = relationService.getByItemId(itemId)

	override fun deleteRelations(data: LocationEntity) {
		relationService.delete(data)
	}
}
