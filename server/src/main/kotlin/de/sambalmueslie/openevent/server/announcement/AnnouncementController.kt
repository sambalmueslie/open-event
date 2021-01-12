package de.sambalmueslie.openevent.server.announcement


import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/api/announcement")
class AnnouncementController(
	userService: UserService,
	private val service: AnnouncementService
) : CrudController<Announcement, AnnouncementChangeRequest>(userService) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AnnouncementController::class.java)
	}

	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{objId}")
	override fun get(authentication: Authentication, @PathVariable objId: Long) =
		service.get(authentication, getUser(authentication), objId)

	@Post()
	override fun create(authentication: Authentication, @Body request: AnnouncementChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{objId}")
	override fun update(authentication: Authentication, @PathVariable objId: Long, @Body request: AnnouncementChangeRequest) =
		service.update(authentication, getUser(authentication), objId, request)

	@Delete("/{objId}")
	override fun delete(authentication: Authentication, @PathVariable objId: Long) =
		service.delete(authentication, getUser(authentication), objId)

}
