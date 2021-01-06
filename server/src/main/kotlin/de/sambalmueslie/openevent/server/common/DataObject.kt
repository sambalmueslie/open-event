package de.sambalmueslie.openevent.server.common

interface DataObject<T : BusinessObject> {
	fun convert(): T
}
