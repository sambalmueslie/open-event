package de.sambalmueslie.openevent.server.messaging.db


import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.user.api.User

data class MessageConvertContent(
	val author: User,
	val recipient: User
) : ConvertContent
