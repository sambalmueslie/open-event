package de.sambalmueslie.openevent.server.structure.db

import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.db.ItemConvertContent
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.user.api.User

data class StructureConvertContent(
	override val owner: User,
	override val description: ItemDescription,
	val location: Location?
) : ItemConvertContent
