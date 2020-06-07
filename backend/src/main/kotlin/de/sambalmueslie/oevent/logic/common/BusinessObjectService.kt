package de.sambalmueslie.oevent.logic.common

import com.sun.jdi.request.InvalidRequestStateException

interface BusinessObjectService<T : BusinessObject, R : BusinessObjectChangeRequest> {

	fun getAll(): List<T>
	fun get(id: Long): T?

	@Throws(InvalidRequestStateException::class)
	fun create(request: R): T
	fun update(id: Long, request: R): T
	fun delete(id: Long)

}
