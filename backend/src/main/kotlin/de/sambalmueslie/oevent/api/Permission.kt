package de.sambalmueslie.oevent.api

import de.sambalmueslie.oevent.common.BusinessObject

data class Permission(
		override val id: Long,
		val name: String
) : BusinessObject
