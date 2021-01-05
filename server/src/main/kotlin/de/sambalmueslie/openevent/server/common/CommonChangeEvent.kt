package de.sambalmueslie.openevent.server.common


class CommonChangeEvent<T : BusinessObject>(
	override val obj: T,
	override val type: CommonChangeEventType
) : BusinessObjectChangeEvent<T, CommonChangeEventType>
