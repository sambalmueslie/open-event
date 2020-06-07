package de.sambalmueslie.oevent.logic.common

interface DataObjectMerger<E : DataObject<*>, R : BusinessObjectChangeRequest> {
	fun merge(existing: E?, request: R, context: DataObjectContext) : E
}
