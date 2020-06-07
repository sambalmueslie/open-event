package de.sambalmueslie.oevent.logic.common

interface DataObject<T : BusinessObject> {
	fun convert(): T
}
