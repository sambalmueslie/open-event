package de.sambalmueslie.openevent.server.common

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface CrudAPI<T : BusinessObject, O : BusinessObjectChangeRequest> {
	fun getAll(authentication: Authentication, pageable: Pageable): Page<T>
	fun get(authentication: Authentication, objId: Long): T?
	fun create(authentication: Authentication, request: O): T?
	fun update(authentication: Authentication, objId: Long, request: O): T?
	fun delete(authentication: Authentication, objId: Long)
}
