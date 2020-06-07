package de.sambalmueslie.oevent.logic.category.api

import de.sambalmueslie.oevent.common.BusinessObject

data class Category(
		override val id: Long,
		val name: String,
		val iconUrl: String
) : BusinessObject
