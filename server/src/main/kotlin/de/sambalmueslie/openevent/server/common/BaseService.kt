package de.sambalmueslie.openevent.server.common


import org.slf4j.Logger

abstract class BaseService<T : BusinessObject>(private val logger: Logger) {

	private val listeners = mutableSetOf<BusinessObjectChangeListener<T>>()

	fun register(listener: BusinessObjectChangeListener<T>) {
		listeners.add(listener)
	}

	fun unregister(listener: BusinessObjectChangeListener<T>) {
		listeners.remove(listener)
	}

	protected fun notifyCommon(event: CommonChangeEvent<T>) {
		listeners.forEachWithTryCatch { it.handleCommonEvent(event) }
	}

	protected fun <O : Any> notify(event: BusinessObjectChangeEvent<T, O>) {
		listeners.forEachWithTryCatch { it.handleChangeEvent(event) }
	}
}
