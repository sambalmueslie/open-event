package de.sambalmueslie.openevent.server.structure


import de.sambalmueslie.openevent.server.item.ItemCrudService
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.location.LocationCrudService
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.structure.db.StructureConvertContent
import de.sambalmueslie.openevent.server.structure.db.StructureData
import de.sambalmueslie.openevent.server.structure.db.StructureRepository
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
open class StructureCrudService(
	private val repository: StructureRepository,
	userService: UserService,
	itemDescriptionCrudService: ItemDescriptionCrudService,
	private val locationCrudService: LocationCrudService
) : ItemCrudService<Structure, StructureChangeRequest, StructureData>(repository, userService, itemDescriptionCrudService, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(StructureCrudService::class.java)
	}

	override fun convert(data: StructureData, owner: User, description: ItemDescription): Structure {
		val location = data.locationId?.let { locationCrudService.get(it) }
		return data.convert(StructureConvertContent(owner, description, location))
	}

	override fun create(user: User, request: StructureChangeRequest, description: ItemDescription): Structure {
		val location = request.location?.let { locationCrudService.create(user, it) }
		val data = StructureData.convert(user, request, description, location)
		return repository.save(data).convert(StructureConvertContent(user, description, location))
	}

	override fun update(user: User, data: StructureData, request: StructureChangeRequest, description: ItemDescription): Structure {
		val location = locationCrudService.update(user, data.locationId, request.location)
		data.update(request, description, location)
		return repository.update(data).convert(StructureConvertContent(user, description, location))
	}


}
