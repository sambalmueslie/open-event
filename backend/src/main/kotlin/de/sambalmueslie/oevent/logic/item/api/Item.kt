package de.sambalmueslie.oevent.logic.item.api

import de.sambalmueslie.oevent.common.BusinessObject

interface Item : BusinessObject {
	override val id: Long
	val title: String
	val shortText: String
	val longText: String
	val imageUrl: String
	val iconUrl: String
}
