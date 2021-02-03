package de.sambalmueslie.openevent.server.common

interface BusinessObjectChangeListener<T : BusinessObject> {
	fun handleCommonEvent(event: CommonChangeEvent<T>)

	fun <O : Any> handleChangeEvent(event: BusinessObjectChangeEvent<T, O>){
		// intentionally left empty
	}
}
