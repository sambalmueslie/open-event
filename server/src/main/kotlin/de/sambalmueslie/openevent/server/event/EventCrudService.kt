package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.db.EventConvertContent
import de.sambalmueslie.openevent.server.event.db.EventData
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.location.LocationCrudService
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class EventCrudService(
	private val repository: EventRepository,
	private val userService: UserService,
	private val itemDescriptionCrudService: ItemDescriptionCrudService,
	private val locationCrudService: LocationCrudService
) : BaseCrudService<Event, EventChangeRequest, EventData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventCrudService::class.java)
	}

	@Transactional
	@Synchronized
	override fun create(user: User, request: EventChangeRequest): Event? {
		val existing = repository.findExisting(user, request)
		if (existing != null) {
			logger.info("Double request detected ${user.id} : $request")
			return update(user, existing, request)
		}
		val description = itemDescriptionCrudService.create(user, request.item)
		val location = request.location?.let { locationCrudService.create(user, it) }
		val data = EventData.convert(user, request, description, location)
		val result = repository.save(data).convert(EventConvertContent(user, description, location))
		notifyCreated(user, result)
		return result
	}

	@Transactional
	@Synchronized
	override fun update(user: User, objId: Long, request: EventChangeRequest): Event? {
		return repository.findByIdOrNull(objId)?.let { update(user, it, request) }
	}

	private fun update(user: User, event: EventData, request: EventChangeRequest): Event {
		val description = itemDescriptionCrudService.update(user, event.descriptionId, request.item)

		val location = if(request.location != null){
			val data = event.locationId?.let { locationCrudService.update(user, it, request.location) } ?: locationCrudService.create(user, request.location)
			event.locationId = data.id
			data
		} else {
			event.locationId?.let { locationCrudService.delete(user, it) }
			event.locationId = null
			null
		}

		val data = EventData.convert(user, request, description, location)
		val result = repository.update(data).convert(EventConvertContent(user, description, location))
		notifyUpdated(user, result)
		return result
	}

	override fun convert(data: EventData): Event {
		val owner = userService.getUser(data.ownerId)
			?: throw IllegalArgumentException("Cannot find owner by ${data.ownerId}")
		val description = itemDescriptionCrudService.get(data.descriptionId)
			?: throw IllegalArgumentException("Cannot find description by ${data.descriptionId}")
		val location = data.locationId?.let { locationCrudService.get(it) }
		return data.convert(EventConvertContent(owner, description, location))
	}


}
