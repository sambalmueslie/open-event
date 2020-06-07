package de.sambalmueslie.oevent.user


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.common.DataObjectValidator
import de.sambalmueslie.oevent.user.api.UserType
import de.sambalmueslie.oevent.user.db.UserData
import de.sambalmueslie.oevent.user.db.UserRepository

class UserValidator(private val repository: UserRepository) : DataObjectValidator<UserData> {
	override fun validate(data: UserData) {
		if (data.email.isBlank()) throw InvalidRequestStateException("Email of user ${data.id} must not be blank.")
		if (data.userName.isBlank()) throw InvalidRequestStateException("Username of user ${data.id} must not be blank.")
		if (data.type == UserType.IDP && data.externalId.isBlank()) throw InvalidRequestStateException("External ID for idp user ${data.id} must not be blank.")

		val mailExisting = repository.findByEmail(data.email)
		if (mailExisting != null) throw InvalidRequestStateException("User already existing.")

		if (data.externalId.isNotBlank()) {
			val externalIdExisting = repository.findByExternalId(data.externalId)
			if (externalIdExisting != null) throw InvalidRequestStateException("User already existing.")
		}

		val userNameExisting = repository.findByUserName(data.userName)
		if (userNameExisting != null) throw InvalidRequestStateException("User already existing.")
	}
}
