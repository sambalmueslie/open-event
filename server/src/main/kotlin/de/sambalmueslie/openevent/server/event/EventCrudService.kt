package de.sambalmueslie.openevent.server.event


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.db.EventData
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.location.LocationCrudService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class EventCrudService(
	private val repository: EventRepository,
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
		val location = request.location?.let { locationCrudService.createData(user, it).first }
		val data = EventData.convert(user, request, description, location)
		val result = repository.save(data).convert()
		notifyCommon(CommonChangeEvent(user, result, CommonChangeEventType.CREATED))
		return result
	}

	override fun update(user: User, objId: Long, request: EventChangeRequest): Event? {
		return repository.findByIdOrNull(objId)?.let { update(user, it, request) }
	}

	private fun update(user: User, event: EventData, request: EventChangeRequest): Event? {
		TODO("Not yet implemented")
	}


}
