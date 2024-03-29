package de.sambalmueslie.openevent.server.user.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
import de.sambalmueslie.openevent.server.user.api.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "User")
@Table(name = "db_user")
data class UserData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	@Column(unique = true)
	val externalId: String = "",
	@Column(nullable = false)
	var userName: String = "",
	@Column(nullable = false)
	var firstName: String = "",
	@Column(nullable = false)
	var lastName: String = "",
	@Column(nullable = false)
	var email: String = "",
	@Column(nullable = false)
	var iconUrl: String = "",
	@Column(nullable = false)
	var lastSync: LocalDateTime = LocalDateTime.now(),
	@Column(nullable = false)
	val serviceUser: Boolean = false
) : DataObject<User, EmptyConvertContent> {

	companion object {
		fun convert(user: User): UserData {
			return UserData(
				user.id,
				user.externalId,
				user.userName,
				user.firstName,
				user.lastName,
				user.email,
				user.iconUrl,
				LocalDateTime.now(),
				user.serviceUser
			)
		}
	}

	fun convert() = convert(EmptyConvertContent())
	override fun convert(content: EmptyConvertContent): User {
		return User(id, externalId, userName, firstName, lastName, email, iconUrl, serviceUser)
	}
}
