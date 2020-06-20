package de.sambalmueslie.oevent.common


import kotlin.reflect.KClass

class DataObjectContext(private val immutable: Boolean = false) {

	companion object {
		val EMPTY = DataObjectContext(true)
	}

	private val dependencies: MutableMap<KClass<out BusinessObject>, BusinessObject> = mutableMapOf()
	private val relations: MutableMap<KClass<out DataObject<*>>, DataObject<*>> = mutableMapOf()

	fun <T : BusinessObject> putDependency(type: KClass<T>, value: T?) {
		if (immutable) throw InvalidChangeRequestException("Cannot modify immutable data object context")
		if (value == null) return
		dependencies[type] = value
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : BusinessObject> getDependency(type: KClass<T>): T? {
		return dependencies[type] as T
	}

	fun <T : BusinessObject, R : DataObject<T>> putRelation(type: KClass<R>, value: R?) {
		if (immutable) throw InvalidChangeRequestException("Cannot modify immutable data object context")
		if (value == null) return
		relations[type] = value
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : BusinessObject, R : DataObject<T>> getRelation(type: KClass<R>): R? {
		return relations[type] as R
	}
}
