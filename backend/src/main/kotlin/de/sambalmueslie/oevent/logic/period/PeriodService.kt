package de.sambalmueslie.oevent.logic.period


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.logic.common.BusinessObjectService
import de.sambalmueslie.oevent.logic.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.period.api.Period
import de.sambalmueslie.oevent.logic.period.api.PeriodChangeRequest
import de.sambalmueslie.oevent.logic.period.db.PeriodData
import de.sambalmueslie.oevent.logic.period.db.PeriodRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class PeriodService(private val repository: PeriodRepository) : BusinessObjectService<Period, PeriodChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(PeriodService::class.java)
	}

	override fun getAll(): List<Period> {
		return repository.findAll().map { it.convert() }
	}

	override fun get(id: Long): Period? {
		return repository.findByIdOrNull(id)?.convert()
	}

	override fun create(request: PeriodChangeRequest): Period {
		return persist(convert(request)).convert()
	}

	override fun update(id: Long, request: PeriodChangeRequest): Period {
		val existing = repository.findByIdOrNull(id) ?: return create(request)

		existing.start = request.start
		existing.end = request.end
		return persist(existing).convert()
	}

	override fun delete(id: Long) {
		repository.deleteById(id)
	}

	private fun convert(request: PeriodChangeRequest): PeriodData {
		return PeriodData(0, request.start, request.end)
	}

	private fun persist(data: PeriodData): PeriodData {
		validate(data)
		return if (data.id == 0L) {
			repository.save(data)
		} else {
			repository.update(data)
		}
	}

	private fun validate(data: PeriodData) {
		if (data.end.isBefore(data.start)) throw InvalidRequestStateException("Cannot update period. Start ${data.start} must be before ${data.end}")
	}
}
