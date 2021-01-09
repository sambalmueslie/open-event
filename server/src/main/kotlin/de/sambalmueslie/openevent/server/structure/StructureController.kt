package de.sambalmueslie.openevent.server.structure


import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/api/structure")
class StructureController(
	userService: UserService,
	private val service: StructureService
) : CrudController<Structure, StructureChangeRequest>(userService) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(StructureController::class.java)
	}

	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{objId}")
	override fun get(authentication: Authentication, @PathVariable objId: Long) =
		service.get(authentication, getUser(authentication), objId)

	@Post()
	override fun create(authentication: Authentication, @Body request: StructureChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{objId}")
	override fun update(authentication: Authentication, @PathVariable objId: Long, @Body request: StructureChangeRequest) =
		service.update(authentication, getUser(authentication), objId, request)


	@Delete("/{objId}")
	override fun delete(authentication: Authentication, @PathVariable objId: Long) =
		service.delete(authentication, getUser(authentication), objId)



}
