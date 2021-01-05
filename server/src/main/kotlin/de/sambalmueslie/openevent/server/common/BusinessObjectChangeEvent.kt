package de.sambalmueslie.openevent.server.common

interface BusinessObjectChangeEvent<T : BusinessObject, O : Any> {
	val type: O
	val obj: T
}
