package de.sambalmueslie.oevent.common

interface DataObjectMerger<E : DataObject<*>, R : BusinessObjectChangeRequest> {
	fun merge(existing: E?, request: R, context: DataObjectContext) : E
}
