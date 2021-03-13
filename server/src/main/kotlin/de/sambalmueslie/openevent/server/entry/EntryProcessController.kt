package de.sambalmueslie.openevent.server.entry


import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessAPI
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/entry")
class EntryProcessController(
	userService: UserService,
	private val service: EntryProcessService
) : CrudController<EntryProcess, EntryProcessChangeRequest>(userService), EntryProcessAPI {


	@Get()
	override fun getAll(authentication: Authentication, pageable: Pageable) =
		service.getAll(authentication, getUser(authentication), pageable)

	@Get("/{objId}")
	override fun get(authentication: Authentication, @PathVariable objId: Long) =
		service.get(authentication, getUser(authentication), objId)

	@Post()
	override fun create(authentication: Authentication, @Body request: EntryProcessChangeRequest) =
		service.create(authentication, getUser(authentication), request)

	@Put("/{objId}")
	override fun update(authentication: Authentication, @PathVariable objId: Long, @Body request: EntryProcessChangeRequest) =
		service.update(authentication, getUser(authentication), objId, request)

	@Delete("/{objId}")
	override fun delete(authentication: Authentication, @PathVariable objId: Long) =
		service.delete(authentication, getUser(authentication), objId)

	@Get("/user/{userId}")
	override fun getUserEntryProcesses(authentication: Authentication, @PathVariable userId: Long, pageable: Pageable) =
		service.getUserEntryProcesses(authentication, getUser(authentication), userId, pageable)

	@Get("/item/{itemId}")
	override fun getItemEntryProcesses(authentication: Authentication, @PathVariable itemId: Long, pageable: Pageable) =
		service.getItemEntryProcesses(authentication, getUser(authentication), itemId, pageable)

	@Delete()
	fun deleteAll(authentication: Authentication) = service.deleteAll(authentication, getUser(authentication))

	@Put("/{processId}/accept")
	override fun accept(authentication: Authentication, processId: Long): EntryProcess? = service.accept(authentication, getUser(authentication), processId)

	@Put("/{processId}/decline")
	override fun decline(authentication: Authentication, processId: Long): EntryProcess? = service.decline(authentication, getUser(authentication), processId)
}
