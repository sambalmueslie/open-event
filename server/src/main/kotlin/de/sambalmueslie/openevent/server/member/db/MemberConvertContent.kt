package de.sambalmueslie.openevent.server.member.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.user.api.User

data class MemberConvertContent(
	val user: User
) : ConvertContent
