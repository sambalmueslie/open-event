package de.sambalmueslie.openevent.server.announcement


import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AnnouncementService(private val crudService: AnnouncementCrudService) : AuthCrudService<Announcement, AnnouncementChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AnnouncementService::class.java)
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Announcement> {
		// TODO check authentication
		return crudService.getAll(pageable)
	}

	override fun get(authentication: Authentication, user: User, objId: Long): Announcement? {
		// TODO check authentication
		return crudService.get(objId)
	}

	override fun create(authentication: Authentication, user: User, request: AnnouncementChangeRequest): Announcement? {
		// TODO check authentication
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, objId: Long, request: AnnouncementChangeRequest): Announcement? {
		// TODO check authentication
		return crudService.update(user, objId, request)
	}

	override fun delete(authentication: Authentication, user: User, objId: Long) {
		// TODO check authentication
		return crudService.delete(user, objId)
	}


}
