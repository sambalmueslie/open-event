package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Conversation
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import javax.persistence.*

//@Entity(name = "Conversation")
//@Table(name = "conversation")
data class ConversationData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@OneToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "item_id")
		val item: ItemData = ItemData()
) : DataObject<Conversation> {

	override fun convert(context: DataObjectContext): Conversation {
		return Conversation(id, author?.convert(context) ?: UserData().convert(),
				recipients.map { it.convert(context) }, item, messages.map { it.convert(context) })
	}

	val messages: Set<MessageData>
		get() = _messages

	@OneToMany(mappedBy = "conversation", cascade = [CascadeType.ALL], orphanRemoval = true)
	private val _messages: MutableSet<MessageData> = mutableSetOf()

	fun add(message: MessageData) {
		_messages.add(message)
		message.conversation = this
	}

	fun remove(message: MessageData) {
		_messages.remove(message)
		message.conversation = null
	}

	val recipients: Set<UserData>
		get() = _recipients

	@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
	@JoinTable(name = "conversation_recipient", joinColumns = [JoinColumn(name = "conversation_id")], inverseJoinColumns = [JoinColumn(name = "recipient_id")])
	private val _recipients: MutableSet<UserData> = mutableSetOf()

	fun add(user: UserData) {
		_recipients.add(user)
		user.add(this)
	}

	fun remove(user: UserData) {
		_recipients.remove(user)
		user.remove(this)
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	var author: UserData? = null

}
