package de.sambalmueslie.openevent.server.entry.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.user.api.User

data class EntryProcessConvertContent(
	val user: User
) : ConvertContent
