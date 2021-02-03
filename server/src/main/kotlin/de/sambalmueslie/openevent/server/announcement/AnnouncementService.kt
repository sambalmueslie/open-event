package de.sambalmueslie.openevent.server.announcement


import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.announcement.db.AnnouncementData
import de.sambalmueslie.openevent.server.announcement.db.AnnouncementRepository
import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.auth.InsufficientPermissionsException
import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AnnouncementService(
	private val crudService: AnnouncementCrudService,
	private val authenticationHelper: AuthenticationHelper,
	private val entitlementService: ItemEntitlementCrudService,
	private val repository: AnnouncementRepository
) : AuthCrudService<Announcement, AnnouncementChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AnnouncementService::class.java)
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Announcement> {
		return if (authenticationHelper.isAdmin(authentication)) {
			crudService.getAll(pageable)
		} else {
			repository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
		}
	}

	fun getItemAnnouncements(authentication: Authentication, user: User, itemId: Long, pageable: Pageable): Page<Announcement> {
		if(!isAccessAllowed(authentication, user, itemId)) return Page.empty()
		return crudService.getForItem(itemId, pageable)
	}

	override fun get(authentication: Authentication, user: User, objId: Long): Announcement? {
		val data = crudService.getData(objId) ?: return null
		if (!isAccessAllowed(authentication, user, data.itemId))
			throw InsufficientPermissionsException("Cannot access announcement due to insufficient permissions")
		return crudService.get(objId)
	}

	override fun create(authentication: Authentication, user: User, request: AnnouncementChangeRequest): Announcement {
		if (!isCreationAllowed(authentication, user, request))
			throw InsufficientPermissionsException("Cannot create announcement due to insufficient permissions")
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, objId: Long, request: AnnouncementChangeRequest): Announcement? {
		val data = crudService.getData(objId) ?: return null
		if (!isModificationAllowed(authentication, user, data.itemId))
			throw InsufficientPermissionsException("Cannot update announcement due to insufficient permissions")
		return crudService.update(user, data, request)
	}

	override fun delete(authentication: Authentication, user: User, objId: Long) {
		val data = crudService.getData(objId) ?: return
		if (!isModificationAllowed(authentication, user, data.itemId))
			throw InsufficientPermissionsException("Cannot update announcement due to insufficient permissions")
		return crudService.delete(user, data)
	}

	private fun isCreationAllowed(authentication: Authentication, user: User, request: AnnouncementChangeRequest): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return getEntitlement(user, request.itemId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
	}


	private fun isModificationAllowed(authentication: Authentication, user: User, itemId: Long): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return getEntitlement(user, itemId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
	}

	private fun isAccessAllowed(authentication: Authentication, user: User, itemId: Long): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return getEntitlement(user, itemId).isGreaterThanEquals(Entitlement.VIEWER)
	}

	private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemId(user.id, objId).entitlement

}
