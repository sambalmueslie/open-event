package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Entitlement
import de.sambalmueslie.oevent.api.Profile
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import javax.persistence.*

@Entity(name = "Entitlement")
@Table(name = "entitlement")
data class EntitlementData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false, unique = true)
		var name: String = ""
) : DataObject<Entitlement> {
	override fun convert(context: DataObjectContext): Entitlement {
		return Entitlement(id, name)
	}

	@ManyToOne(fetch = FetchType.LAZY)
	var profile: ProfileData? = null

	val members: Set<MemberData>
		get() = _members

	@OneToMany(mappedBy = "entitlement", cascade = [CascadeType.ALL], orphanRemoval = true)
	private val _members: MutableSet<MemberData> = mutableSetOf()

	fun add(member: MemberData) {
		_members.add(member)
		member.entitlement = this
	}

	fun remove(member: MemberData) {
		_members.remove(member)
		member.entitlement = null
	}

	val items: Set<ItemData>
		get() = _items

	@ManyToMany(mappedBy = "_entitlements")
	private val _items: MutableSet<ItemData> = mutableSetOf()

	fun add(item: ItemData) {
		_items.add(item)
	}

	fun remove(item: ItemData) {
		_items.remove(item)
	}

}
