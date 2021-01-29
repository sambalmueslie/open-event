package de.sambalmueslie.openevent.server.structure


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.auth.InsufficientPermissionsException
import de.sambalmueslie.openevent.server.common.AuthCrudService
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.structure.db.StructureRepository
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class StructureService(
	private val crudService: StructureCrudService,
	private val authenticationHelper: AuthenticationHelper,
	private val entitlementService: ItemEntitlementCrudService,
	private val structureRepository: StructureRepository,
) : AuthCrudService<Structure, StructureChangeRequest> {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(StructureService::class.java)
	}

	fun getRoots(authentication: Authentication, user: User, pageable: Pageable): Page<Structure> {
		return if (authenticationHelper.isAdmin(authentication)) {
			structureRepository.findByParentStructureIdIsNull(pageable)
		} else {
			structureRepository.getAllRootAccessible(user.id, pageable)
		}.map { crudService.convert(it) }
	}

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<Structure> {
		return if (authenticationHelper.isAdmin(authentication)) {
			crudService.getAll(pageable)
		} else {
			structureRepository.getAllAccessible(user.id, pageable).map { crudService.convert(it) }
		}
	}

	override fun get(authentication: Authentication, user: User, objId: Long): Structure? {
		if (!isAccessAllowed(authentication, user, objId))
			throw InsufficientPermissionsException("Cannot access structure due to insufficient permissions")
		return crudService.get(objId)
	}

	fun getChildren(authentication: Authentication, user: User, objId: Long, pageable: Pageable): Page<Structure> {
		return if (authenticationHelper.isAdmin(authentication)) {
			structureRepository.findByParentStructureId(objId,pageable)
		} else {
			structureRepository.getAllChildAccessible(user.id, objId, pageable)
		}.map { crudService.convert(it) }
	}

	override fun create(authentication: Authentication, user: User, request: StructureChangeRequest): Structure? {
		if (!isCreationAllowed(authentication, user, request))
			throw InsufficientPermissionsException("Cannot create structure due to insufficient permissions")
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, objId: Long, request: StructureChangeRequest): Structure? {
		if (!isModificationAllowed(authentication, user, objId))
			throw InsufficientPermissionsException("Cannot update structure due to insufficient permissions")
		return crudService.update(user, objId, request)
	}

	override fun delete(authentication: Authentication, user: User, objId: Long) {
		if (!isModificationAllowed(authentication, user, objId))
			throw InsufficientPermissionsException("Cannot delete structure due to insufficient permissions")
		return crudService.delete(user, objId)
	}

	private fun isCreationAllowed(authentication: Authentication, user: User, request: StructureChangeRequest): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		val parentId = request.parentStructureId ?: return false
		return getEntitlement(user, parentId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
	}

	private fun isModificationAllowed(authentication: Authentication, user: User, objId: Long): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return getEntitlement(user, objId).isGreaterThanEquals(Entitlement.ADMINISTRATOR)
	}

	private fun isAccessAllowed(authentication: Authentication, user: User, objId: Long): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return getEntitlement(user, objId).isGreaterThanEquals(Entitlement.VIEWER)
	}

	private fun getEntitlement(user: User, objId: Long) = entitlementService.findByUserIdAndItemIdAndType(user.id, objId, ItemType.STRUCTURE).entitlement


}
