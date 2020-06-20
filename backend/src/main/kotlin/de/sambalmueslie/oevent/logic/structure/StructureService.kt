package de.sambalmueslie.oevent.logic.structure


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.item.ItemService
import de.sambalmueslie.oevent.logic.location.LocationService
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.logic.structure.api.Structure
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import de.sambalmueslie.oevent.model.StructureData
import de.sambalmueslie.oevent.logic.structure.db.StructureRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class StructureService(
		private val repository: StructureRepository,
		private val itemService: ItemService,
		private val locationService: LocationService
) : BaseService<Structure, StructureChangeRequest, StructureData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(StructureService::class.java)
	}

	private val validator = StructureValidator()
	override fun getValidator() = validator

	private val merger = StructureMerger()
	override fun getMerger() = merger


	override fun saveDependencies(data: StructureData, request: StructureChangeRequest, context: DataObjectContext) {
		itemService.merge(request.item, data)

		updateParent(request, data, context)

		val location = request.location?.let { locationService.create(it) }
		context.putDependency(Location::class, location)
	}

	override fun updateDependencies(data: StructureData, request: StructureChangeRequest, context: DataObjectContext) {
		itemService.merge(request.item, data)

		updateParent(request, data, context)

		val location = request.location?.let { locationService.update(data.id, it) }
		context.putDependency(Location::class, location)
	}

	override fun saveRelations(data: StructureData, context: DataObjectContext) {
		val parent = context.getRelation(StructureData::class) ?: return

		parent.children.add(data)
		repository.save(parent)
	}

	override fun updateRelations(data: StructureData, context: DataObjectContext) {
		val parent = context.getRelation(StructureData::class) ?: return

		parent.children.add(data)
		repository.save(parent)
	}

	private fun updateParent(request: StructureChangeRequest, data: StructureData, context: DataObjectContext) {
		val parent = request.parentStructureId?.let { repository.findByIdOrNull(request.parentStructureId) }
		if (parent != null) {
			data.parent.add(parent)
			context.putRelation(StructureData::class, parent)
		}
	}


}
