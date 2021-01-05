package de.sambalmueslie.openevent.server.item.api

import de.sambalmueslie.openevent.server.common.BusinessObject

interface Item : BusinessObject {
	val userId: Long
	val description: ItemDescription
}
