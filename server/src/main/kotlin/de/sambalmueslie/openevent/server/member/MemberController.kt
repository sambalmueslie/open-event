package de.sambalmueslie.openevent.server.member


import de.sambalmueslie.openevent.server.common.CrudController
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.member.api.MemberAPI
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import de.sambalmueslie.openevent.server.user.UserService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication

@Controller("/api/member")
class MemberController(
    userService: UserService,
    private val service: MemberService
) : CrudController<Member, MemberChangeRequest>(userService), MemberAPI {

    @Get()
    override fun getAll(authentication: Authentication, pageable: Pageable) =
        service.getAll(authentication, getUser(authentication), pageable)

    @Get("/{objId}")
    override fun get(authentication: Authentication, @PathVariable objId: Long) =
        service.get(authentication, getUser(authentication), objId)

    @Post()
    override fun create(authentication: Authentication, @Body request: MemberChangeRequest) =
        service.create(authentication, getUser(authentication), request)

    @Put("/{objId}")
    override fun update(authentication: Authentication, @PathVariable objId: Long, @Body request: MemberChangeRequest) =
        service.update(authentication, getUser(authentication), objId, request)

    @Delete("/{objId}")
    override fun delete(authentication: Authentication, @PathVariable objId: Long) =
        service.delete(authentication, getUser(authentication), objId)


    @Get("/user/{userId}")
    override fun getUserMembers(authentication: Authentication, @PathVariable userId: Long, pageable: Pageable) =
        service.getUserMembers(authentication, getUser(authentication), userId, pageable)

    @Get("/item/{itemId}")
    override fun getItemMembers(authentication: Authentication, @PathVariable itemId: Long, pageable: Pageable) =
        service.getItemMembers(authentication, getUser(authentication), itemId, pageable)

}
