package de.sambalmueslie.openevent.server.member


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.common.BaseAuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import de.sambalmueslie.openevent.server.member.db.MemberData
import de.sambalmueslie.openevent.server.member.db.MemberRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class MemberService(
    private val crudService: MemberCrudService,
    private val authenticationHelper: AuthenticationHelper,
    private val entitlementService: ItemEntitlementCrudService,
    private val repository: MemberRepository
) : BaseAuthCrudService<Member, MemberChangeRequest, MemberData>(crudService, authenticationHelper, logger) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(MemberService::class.java)
    }

    override fun getAllAccessible(user: User, pageable: Pageable): Page<Member> {
        return repository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
    }

    override fun isAccessAllowed(user: User, obj: MemberData): Boolean {
        return user.id == obj.userId || getEntitlement(user, obj.itemId).isGreaterThanEquals(Entitlement.MANAGER)
    }

    override fun isCreationAllowed(user: User, request: MemberChangeRequest): Boolean {
        return getEntitlement(user, request.itemId).isGreaterThanEquals(Entitlement.MANAGER)
    }

    override fun isModificationAllowed(user: User, obj: MemberData): Boolean {
        return obj.userId == user.id || getEntitlement(user, obj.itemId).isGreaterThanEquals(Entitlement.MANAGER)
    }


    fun deleteAll(authentication: Authentication, user: User) {
        if (authenticationHelper.isAdmin(authentication)) {
            repository.deleteAll()
        }
    }

    private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemId(user.id, objId).entitlement

    fun getUserMembers(authentication: Authentication, user: User, userId: Long, pageable: Pageable): Page<Member> {
        return if (authenticationHelper.isAdmin(authentication) || user.id == userId) {
            repository.findByUserId(userId, pageable).map { crudService.convert(it) }
        } else {
            Page.empty()
        }
    }

    fun getItemMembers(authentication: Authentication, user: User, itemId: Long, pageable: Pageable): Page<Member> {
        return if (authenticationHelper.isAdmin(authentication) || getEntitlement(user, itemId).isGreaterThanEquals(Entitlement.MANAGER)) {
            repository.findByItemId(itemId, pageable).map { crudService.convert(it) }
        } else {
            Page.empty()
        }
    }
}
