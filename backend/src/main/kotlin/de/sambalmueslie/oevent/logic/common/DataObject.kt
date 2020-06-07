package de.sambalmueslie.oevent.logic.common

interface DataObject<T : BusinessObject> {
	val id: Long
	fun convert(dependencies: DataObjectContext =  DataObjectContext.EMPTY): T
}
