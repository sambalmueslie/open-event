package de.sambalmueslie.openevent.server.item.api

import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.user.api.User

interface Item : BusinessObject {
	val owner: User
	val description: ItemDescription
}
