package de.sambalmueslie.oevent.logic.category.api

import de.sambalmueslie.oevent.logic.common.BusinessObjectChangeRequest

data class CategoryChangeRequest(
		val name: String,
		val iconUrl: String
) : BusinessObjectChangeRequest
