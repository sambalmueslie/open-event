package de.sambalmueslie.openevent.server.common

import de.sambalmueslie.openevent.server.user.api.User


class CommonChangeEvent<T : BusinessObject>(
	val user: User?,
	override val obj: T,
	override val type: CommonChangeEventType,
) : BusinessObjectChangeEvent<T, CommonChangeEventType>
