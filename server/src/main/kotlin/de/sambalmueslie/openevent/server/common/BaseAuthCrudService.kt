package de.sambalmueslie.openevent.server.common


import de.sambalmueslie.openevent.server.auth.AuthenticationHelper
import de.sambalmueslie.openevent.server.auth.InsufficientPermissionsException
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger

abstract class BaseAuthCrudService<T : BusinessObject, O : BusinessObjectChangeRequest, D : DataObject<T, out ConvertContent>>(
	private val crudService: CrudService<T, O, D>,
	private val authenticationHelper: AuthenticationHelper,
	private val logger: Logger
) : AuthCrudService<T, O> {

	override fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<T> {
		return if (authenticationHelper.isAdmin(authentication)) {
			crudService.getAll(pageable)
		} else {
			getAllAccessible(user, pageable)
		}
	}

	protected abstract fun getAllAccessible(user: User, pageable: Pageable): Page<T>

	override fun get(authentication: Authentication, user: User, objId: Long): T? {
		val data = crudService.getData(objId) ?: return null
		if (!isAccessAllowed(authentication, user, data))
			throw InsufficientPermissionsException("Cannot access obj due to insufficient permissions")
		return crudService.get(objId)
	}

	override fun create(authentication: Authentication, user: User, request: O): T? {
		if (!isCreationAllowed(authentication, user, request))
			throw InsufficientPermissionsException("Cannot create obj due to insufficient permissions")
		return crudService.create(user, request)
	}

	override fun update(authentication: Authentication, user: User, objId: Long, request: O): T? {
		val data = crudService.getData(objId) ?: return null
		if (!isModificationAllowed(authentication, user, data))
			throw InsufficientPermissionsException("Cannot update obj due to insufficient permissions")
		return crudService.update(user, data, request)
	}

	override fun delete(authentication: Authentication, user: User, objId: Long) {
		val data = crudService.getData(objId) ?: return
		if (!isModificationAllowed(authentication, user, data))
			throw InsufficientPermissionsException("Cannot delete obj due to insufficient permissions")
		return crudService.delete(user, data)
	}

	private fun isCreationAllowed(authentication: Authentication, user: User, request: O): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return isCreationAllowed(user, request)
	}

	protected abstract fun isCreationAllowed(user: User, request: O): Boolean


	private fun isModificationAllowed(authentication: Authentication, user: User, obj: D): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return isModificationAllowed(user, obj)
	}

	protected abstract fun isModificationAllowed(user: User, obj: D): Boolean

	private fun isAccessAllowed(authentication: Authentication, user: User, obj: D): Boolean {
		if (authenticationHelper.isAdmin(authentication)) return true
		return isAccessAllowed(user, obj)
	}

	protected abstract fun isAccessAllowed(user: User, obj: D): Boolean
}
