package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.User
import de.sambalmueslie.oevent.api.UserType
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import io.micronaut.http.annotation.Post
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.persistence.*


@Entity(name = "User")
@Table(name = "_user")
data class UserData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
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
		var lastSync: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
		@Column(nullable = false)
		@Enumerated(EnumType.STRING)
		val type: UserType = UserType.IDP
) : DataObject<User> {

	override fun convert(context: DataObjectContext): User {
		return User(id, externalId, userName, firstName, lastName, email, iconUrl, type)
	}

	val receivedConversations: Set<ConversationData>
		get() = _receivedConversations

	@ManyToMany(mappedBy = "_recipients")
	private val _receivedConversations: MutableSet<ConversationData> = mutableSetOf()

	fun add(item: ConversationData) {
		_receivedConversations.add(item)
	}

	fun remove(item: ConversationData) {
		_receivedConversations.remove(item)
	}

	val authorConversations: Set<ConversationData>
		get() = _authorConversations

	@OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], orphanRemoval = true)
	private val _authorConversations: MutableSet<ConversationData> = mutableSetOf()

	fun addAuthor(permission: ConversationData) {
		_authorConversations.add(permission)
		permission.author = this
	}

	fun removeAuthor(permission: ConversationData) {
		_authorConversations.remove(permission)
		permission.author = null
	}
}
