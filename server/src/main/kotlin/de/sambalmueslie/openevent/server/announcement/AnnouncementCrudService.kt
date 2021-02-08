package de.sambalmueslie.openevent.server.announcement


import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.announcement.db.AnnouncementConvertContent
import de.sambalmueslie.openevent.server.announcement.db.AnnouncementData
import de.sambalmueslie.openevent.server.announcement.db.AnnouncementRepository
import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.PageableIterator
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AnnouncementCrudService(
	private val repository: AnnouncementRepository,
	private val userService: UserService,
) : BaseCrudService<Announcement, AnnouncementChangeRequest, AnnouncementData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AnnouncementCrudService::class.java)
	}

	override fun convert(data: AnnouncementData): Announcement {
		val author = userService.getUser(data.authorId)!!
		return data.convert(AnnouncementConvertContent(author))
	}

	override fun create(user: User, request: AnnouncementChangeRequest): Announcement {
		val data = AnnouncementData.convert(user, request)
		val result = repository.save(data).convert(AnnouncementConvertContent(user))
		notifyCreated(user, result)
		return result
	}

	override fun update(user: User, objId: Long, request: AnnouncementChangeRequest): Announcement? {
		val existing = repository.findByIdOrNull(objId) ?: return create(user, request)
		if (existing.itemId != request.itemId) return create(user, request)
		return update(user, existing, request)
	}

	override fun update(user: User, obj: AnnouncementData, request: AnnouncementChangeRequest): Announcement? {
		val author = userService.getUser(obj.authorId) ?: return null
		obj.update(request)
		val result = repository.update(obj).convert(AnnouncementConvertContent(author))
		notifyUpdated(user, result)
		return result
	}

	fun getForItem(itemId: Long, pageable: Pageable): Page<Announcement> {
		val result = repository.findByItemId(itemId, pageable)
		return result.map { convert(it) }
	}

	fun deleteAllForItem(user: User, itemId: Long) {
		val iterator = PageableIterator{ repository.findByItemId(itemId, it) }
		iterator.forEach { delete(user, it) }
	}


}
