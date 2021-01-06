package de.sambalmueslie.openevent.server.common

import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface CrudService<T : BusinessObject, O : BusinessObjectChangeRequest> {
	fun getAll(pageable: Pageable): Page<T>

	fun get(objId: Long): T?

	fun create(user: User, request: O): T?

	fun update(user: User, objId: Long, request: O): T?

	fun delete(user: User, objId: Long)
}
