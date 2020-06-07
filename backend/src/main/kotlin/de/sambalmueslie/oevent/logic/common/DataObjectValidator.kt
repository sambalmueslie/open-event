package de.sambalmueslie.oevent.logic.common

import com.sun.jdi.request.InvalidRequestStateException

interface DataObjectValidator<E : DataObject<*>> {
	@Throws(InvalidRequestStateException::class)
	fun validate(data: E)
}
