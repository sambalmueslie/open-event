package de.sambalmueslie.oevent.logic.event


import de.sambalmueslie.oevent.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.event.api.Event
import de.sambalmueslie.oevent.logic.event.api.EventChangeRequest
import de.sambalmueslie.oevent.logic.event.db.EventEntity
import de.sambalmueslie.oevent.logic.event.db.EventRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EventService(private val repository: EventRepository) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
	}

	fun get(id: Long): Event? {
		return repository.findByIdOrNull(id)?.convert()
	}

	fun getAll(pageable: Pageable): Page<Event> {
		return repository.findAll(pageable).map { it.convert() }
	}

	fun create(request: EventChangeRequest): Event {
		logger.debug("Create new event $request")
		val entity = EventEntity(request.start, request.stop, request.title,  request.shortText, request.longText, request.imageUrl, request.iconUrl)
		return repository.save(entity).convert()
	}

	fun update(id: Long, request: EventChangeRequest): Event? {
		logger.debug("Update event $id with $request")
		val entity = repository.findByIdOrNull(id) ?: return create(request)
		return repository.save(entity).convert()
	}

	fun delete(id: Long) {
		repository.deleteById(id)
	}

}
