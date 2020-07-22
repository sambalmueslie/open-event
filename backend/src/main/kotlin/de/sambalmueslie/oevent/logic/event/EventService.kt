package de.sambalmueslie.oevent.logic.event


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.event.api.Event
import de.sambalmueslie.oevent.logic.event.api.EventChangeRequest
import de.sambalmueslie.oevent.logic.event.db.EventEntity
import de.sambalmueslie.oevent.logic.event.db.EventRepository
import de.sambalmueslie.oevent.logic.structure.StructureMerger
import de.sambalmueslie.oevent.logic.structure.StructureService
import de.sambalmueslie.oevent.logic.structure.StructureValidator
import de.sambalmueslie.oevent.logic.structure.api.Structure
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import de.sambalmueslie.oevent.logic.structure.db.StructureEntity
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class EventService(private val repository: EventRepository)
	: BaseService<Event, EventChangeRequest, EventEntity>(repository, StructureService.logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventService::class.java)
	}

	private val validator = EventValidator()
	override fun getValidator() = validator

	private val merger = EventMerger()
	override fun getMerger() = merger


}
