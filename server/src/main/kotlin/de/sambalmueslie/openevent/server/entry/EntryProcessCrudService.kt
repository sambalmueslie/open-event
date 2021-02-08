package de.sambalmueslie.openevent.server.entry


import de.sambalmueslie.openevent.server.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.PageableIterator
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.db.EntryProcessConvertContent
import de.sambalmueslie.openevent.server.entry.db.EntryProcessData
import de.sambalmueslie.openevent.server.entry.db.EntryProcessRepository
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
open class EntryProcessCrudService(
	private val repository: EntryProcessRepository,
	private val userService: UserService,
) : BaseCrudService<EntryProcess, EntryProcessChangeRequest, EntryProcessData>(repository, logger){

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EntryProcessCrudService::class.java)
	}

	override fun convert(data: EntryProcessData): EntryProcess {
		val user = userService.getUser(data.userId)!!
		return data.convert(EntryProcessConvertContent(user))
	}

	override fun create(user: User, request: EntryProcessChangeRequest): EntryProcess? {
		val data = EntryProcessData.convert(user, request)
		val result = repository.save(data).convert(EntryProcessConvertContent(user))
		notifyCreated(user, result)
		return result
	}

	override fun update(user: User, objId: Long, request: EntryProcessChangeRequest): EntryProcess? {
		val existing = repository.findByIdOrNull(objId) ?: return create(user, request)
		if (existing.itemId != request.itemId || existing.type != request.type) return create(user, request)
		return update(user, existing, request)
	}

	override fun update(user: User, obj: EntryProcessData, request: EntryProcessChangeRequest): EntryProcess? {
		val processUser = userService.getUser(obj.userId) ?: return null
		obj.update(request)
		val result = repository.update(obj).convert(EntryProcessConvertContent(processUser))
		notifyUpdated(processUser, result)
		return result
	}


	fun deleteAllForItem(user: User, itemId: Long) {
		val iterator = PageableIterator{ repository.findByItemId(itemId, it) }
		iterator.forEach { delete(user, it) }
	}


}
