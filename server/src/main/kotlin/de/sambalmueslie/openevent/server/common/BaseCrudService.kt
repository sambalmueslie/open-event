package de.sambalmueslie.openevent.server.common


import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseCrudService<T : BusinessObject, O : BusinessObjectChangeRequest, D : DataObject<T>>(
	private val repository: PageableRepository<D, Long>,
	private val logger: Logger
) : BaseService<T>(logger), CrudService<T, O> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(BaseCrudService::class.java)
	}

	override fun get(objId: Long): T? {
		return repository.findByIdOrNull(objId)?.convert()
	}

	override fun getAll(pageable: Pageable): Page<T> {
		return repository.findAll(pageable).map { it.convert() }
	}

	final override fun delete(user: User, objId: Long) {
		val data = repository.findByIdOrNull(objId) ?: return logger.error("Cannot delete unknown element $objId")
		repository.deleteById(objId)
		handleDeletion(user, data)
		notifyCommon(CommonChangeEvent(data.convert(), CommonChangeEventType.DELETED))
	}

	protected fun handleDeletion(user: User, data: D) {
		// intentionally left empty
	}

}
