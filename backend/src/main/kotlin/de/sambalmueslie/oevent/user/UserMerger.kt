package de.sambalmueslie.oevent.user

import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.user.api.UserChangeRequest
import de.sambalmueslie.oevent.user.db.UserData
import java.time.LocalDateTime
import java.time.ZoneOffset

class UserMerger : DataObjectMerger<UserData, UserChangeRequest> {
	override fun merge(existing: UserData?, request: UserChangeRequest, context: DataObjectContext): UserData {
		val data = existing ?: UserData()
		data.externalId = request.externalId
		data.email = request.email
		data.userName = request.userName
		data.firstName = request.firstName
		data.lastName = request.lastName
		data.iconUrl = request.iconUrl
		data.lastSync = LocalDateTime.now(ZoneOffset.UTC)
		data.type = request.type
		return data
	}
}
