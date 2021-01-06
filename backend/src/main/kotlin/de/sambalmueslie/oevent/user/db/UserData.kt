package de.sambalmueslie.oevent.user.db

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.user.api.User
import de.sambalmueslie.oevent.user.api.UserType
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.*

@Entity(name = "User")
@Table(name = "_user")
data class UserData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(unique = true)
		var externalId: String = "",
		@Column(nullable = false, unique = true)
		var email: String = "",
		@Column(nullable = false, unique = true)
		var userName: String = "",
		@Column(nullable = false)
		var firstName: String = "",
		@Column(nullable = false)
		var lastName: String = "",
		@Column(nullable = false)
		var iconUrl: String = "",
		@Column(nullable = false)
		var lastSync: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
		@Column(nullable = false)
		@Enumerated(EnumType.STRING)
		var type: UserType = UserType.IDP
) : DataObject<User> {

	override fun convert(context: DataObjectContext): User {
		return User(id, externalId, userName, firstName, lastName, email, iconUrl, type)
	}
}
