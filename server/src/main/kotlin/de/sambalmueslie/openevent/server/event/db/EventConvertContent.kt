package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.user.api.User

data class EventConvertContent(
	val owner: User,
	val description: ItemDescription,
	val location: Location?
) : ConvertContent
