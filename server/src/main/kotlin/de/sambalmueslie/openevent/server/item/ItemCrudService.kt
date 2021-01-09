package de.sambalmueslie.openevent.server.item


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.item.api.Item
import de.sambalmueslie.openevent.server.item.api.ItemChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.db.ItemConvertContent
import de.sambalmueslie.openevent.server.item.db.ItemDataObject
import de.sambalmueslie.openevent.server.item.db.ItemRepository
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import javax.transaction.Transactional

abstract class ItemCrudService<T : Item, O : ItemChangeRequest, D : ItemDataObject<T, out ItemConvertContent>>(
	private val repository: ItemRepository<D>,
	private val userService: UserService,
	private val itemDescriptionCrudService: ItemDescriptionCrudService,
	private val logger: Logger,
) : BaseCrudService<T, O, D>(repository, logger) {


	final override fun convert(data: D): T {
		val owner = userService.getUser(data.ownerId)
			?: throw IllegalArgumentException("Cannot find owner by ${data.ownerId}")
		val description = itemDescriptionCrudService.get(data.descriptionId)
			?: throw IllegalArgumentException("Cannot find description by ${data.descriptionId}")
		return convert(data, owner, description)
	}

	abstract fun convert(data: D, owner: User, description: ItemDescription): T

	@Transactional
	@Synchronized
	override fun create(user: User, request: O): T? {
		val existing = repository.findExisting(user, request)
		if (existing != null) {
			logger.info("Double request detected ${user.id} : $request")
			return update(user, existing, request)
		}
		val description = itemDescriptionCrudService.create(user, request.item)
		val result = create(user, request, description)
		notifyCreated(user, result)
		return get(result.id)
	}

	protected abstract fun create(user: User, request: O, description: ItemDescription): T


	@Transactional
	@Synchronized
	override fun update(user: User, objId: Long, request: O): T? {
		return repository.findByIdOrNull(objId)?.let { update(user, it, request) }
	}

	private fun update(user: User, data: D, request: O): T? {
		val description = itemDescriptionCrudService.update(user, data.descriptionId, request.item)
		val result = update(user, data, request, description)
		notifyUpdated(user, result)
		return get(result.id)
	}

	protected abstract fun update(user: User, data: D, request: O, description: ItemDescription): T
}
