package de.sambalmueslie.openevent.server.item.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.user.api.User

interface ItemConvertContent : ConvertContent {
	val owner: User
	val description: ItemDescription
}
