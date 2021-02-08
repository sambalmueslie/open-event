package de.sambalmueslie.openevent.server.entry.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User
import javax.persistence.*

@Entity(name = "EntryProcess")
@Table(name = "entry_process")
data class EntryProcessData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	@Column(nullable = false)
	val userId: Long = 0,
	@Column(nullable = false)
	var itemId: Long = 0L,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var type: ItemType = ItemType.OTHER,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var status: EntryProcessStatus = EntryProcessStatus.DENIED,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var entitlement: Entitlement = Entitlement.VIEWER
) : DataObject<EntryProcess, EntryProcessConvertContent> {

	companion object {
		fun convert(user: User, request: EntryProcessChangeRequest) =
			EntryProcessData(0L, user.id, request.itemId, request.type, EntryProcessStatus.REQUESTED, request.entitlement)
	}

	override fun convert(content: EntryProcessConvertContent) = EntryProcess(id, content.user, itemId, type, entitlement, status)
	fun update(request: EntryProcessChangeRequest) {
		entitlement = request.entitlement
		status = EntryProcessStatus.REQUESTED
	}

}
