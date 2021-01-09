package de.sambalmueslie.openevent.server.common

import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface AuthCrudService<T : BusinessObject, O : BusinessObjectChangeRequest> {
	fun getAll(authentication: Authentication, user: User, pageable: Pageable): Page<T>

	fun get(authentication: Authentication, user: User, objId: Long): T?

	fun create(authentication: Authentication, user: User, request: O): T?

	fun update(authentication: Authentication, user: User, objId: Long, request: O): T?

	fun delete(authentication: Authentication, user: User, objId: Long)
}
