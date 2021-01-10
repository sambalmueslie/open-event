package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.db.EventConvertContent
import de.sambalmueslie.openevent.server.event.db.EventData
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.item.ItemCrudService
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.location.LocationCrudService
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
open class EventCrudService(
	private val repository: EventRepository,
	userService: UserService,
	itemDescriptionCrudService: ItemDescriptionCrudService,
	private val locationCrudService: LocationCrudService
) : ItemCrudService<Event, EventChangeRequest, EventData>(repository, userService, itemDescriptionCrudService, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventCrudService::class.java)
	}


	override fun create(user: User, request: EventChangeRequest, description: ItemDescription): Event {
		val location = request.location?.let { locationCrudService.create(user, it) }
		val data = EventData.convert(user, request, description, location)
		return repository.save(data).convert(EventConvertContent(user, description, location))
	}


	override fun update(user: User, data: EventData, request: EventChangeRequest, description: ItemDescription): Event {
		val location = locationCrudService.update(user, data.locationId, request.location)
		data.update(request, description, location)
		return repository.update(data).convert(EventConvertContent(user, description, location))
	}

	override fun convert(data: EventData, owner: User, description: ItemDescription): Event {
		val location = data.locationId?.let { locationCrudService.get(it) }
		return data.convert(EventConvertContent(owner, description, location))
	}

}
