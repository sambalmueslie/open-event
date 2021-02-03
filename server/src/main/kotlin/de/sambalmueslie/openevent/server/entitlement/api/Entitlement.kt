package de.sambalmueslie.openevent.server.entitlement.api


enum class Entitlement(val level: Int)  {
	NONE(0),
	VIEWER(1),
	EDITOR(2),
	MANAGER(3),
	ADMINISTRATOR(4);

	fun max(second: Entitlement): Entitlement {
		if (this.level >= second.level) return this
		return second
	}

	fun isGreaterThanEquals(second: Entitlement): Boolean {
		return this.level >= second.level
	}
}
