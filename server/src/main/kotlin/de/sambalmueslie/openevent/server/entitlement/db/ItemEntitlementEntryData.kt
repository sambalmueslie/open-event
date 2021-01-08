package de.sambalmueslie.openevent.server.entitlement.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.db.UserData
import javax.persistence.*

@Entity(name = "ItemEntitlementEntry")
@Table(name = "item_entitlement_entry")
data class ItemEntitlementEntryData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column(nullable = false)
	var userId: Long = 0L,
	@Column(nullable = false)
	var itemId: Long = 0L,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	val type: ItemType = ItemType.OTHER,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var entitlement: Entitlement = Entitlement.VIEWER
) : DataObject<ItemEntitlementEntry, EmptyConvertContent> {

	companion object {
		fun convert(user: User, request: ItemEntitlementChangeRequest) =
			ItemEntitlementEntryData(0L, user.id, request.itemId, request.type, request.entitlement)
	}

	fun convert() = convert(EmptyConvertContent())

	override fun convert(content: EmptyConvertContent) = ItemEntitlementEntry(id, userId, itemId, type, entitlement)
}
