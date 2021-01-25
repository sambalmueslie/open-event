package de.sambalmueslie.openevent.server.common


import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseCrudService<T : BusinessObject, O : BusinessObjectChangeRequest, D : DataObject<T, out ConvertContent>>(
	private val repository: PageableRepository<D, Long>,
	private val logger: Logger
) : BaseService<T>(logger), CrudService<T, O, D> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(BaseCrudService::class.java)
	}

	override fun get(objId: Long) = getData(objId)?.let { convert(it) }
	override fun getData(objId: Long) = repository.findByIdOrNull(objId)
	abstract fun convert(data: D): T

	override fun getAll(pageable: Pageable): Page<T> {
		return repository.findAll(pageable).map { convert(it) }
	}

	final override fun delete(user: User, objId: Long) {
		val data = repository.findByIdOrNull(objId) ?: return logger.error("Cannot delete unknown element $objId")
		delete(user, data)
	}

	final override fun delete(user: User, obj: D) {
		prepareDeletion(user, obj)
		repository.delete(obj)
		handleDeletion(user, obj)
		notifyDeleted(user, convert(obj))
	}

	protected open fun prepareDeletion(user: User, data: D) {
		// intentionally left empty
	}

	protected open fun handleDeletion(user: User, data: D) {
		// intentionally left empty
	}

	protected open fun notifyCreated(user: User, result: T) {
		notifyCommon(CommonChangeEvent(user, result, CommonChangeEventType.CREATED))
	}

	protected  open fun notifyUpdated(user: User, result: T) {
		notifyCommon(CommonChangeEvent(user, result, CommonChangeEventType.UPDATED))
	}

	protected  open fun notifyDeleted(user: User, result: T) {
		notifyCommon(CommonChangeEvent(user, result, CommonChangeEventType.DELETED))
	}

}
