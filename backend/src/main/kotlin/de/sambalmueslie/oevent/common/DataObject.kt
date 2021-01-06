package de.sambalmueslie.oevent.common

interface DataObject<T : BusinessObject> {
	val id: Long
	fun convert(context: DataObjectContext = DataObjectContext.EMPTY): T
}
