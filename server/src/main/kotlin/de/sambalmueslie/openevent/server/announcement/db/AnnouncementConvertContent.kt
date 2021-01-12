package de.sambalmueslie.openevent.server.announcement.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.user.api.User

data class AnnouncementConvertContent(
	val author: User
) : ConvertContent
