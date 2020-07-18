package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.logic.item.api.Item
import javax.persistence.*


//@Entity(name = "Item")
//@Table(name = "item")
//@Inheritance(strategy = InheritanceType.JOINED)
open class ItemData : Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0

	@Column
	override var title: String = ""

	@Column(columnDefinition = "TEXT")
	override var shortText: String = ""

	@Column(columnDefinition = "TEXT")
	override var longText: String = ""

	@Column
	override var imageUrl: String = ""

	@Column
	override var iconUrl: String = ""

	val categories: Set<CategoryData>
		get() = _categories

	@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
	@JoinTable(name = "item_category", joinColumns = [JoinColumn(name = "item_id")], inverseJoinColumns = [JoinColumn(name = "cat_id")])
	private val _categories: MutableSet<CategoryData> = mutableSetOf()

	fun add(category: CategoryData) {
		_categories.add(category)
		category.add(this)
	}

	fun remove(category: CategoryData) {
		_categories.remove(category)
		category.remove(this)
	}

	val entitlements: Set<EntitlementData>
		get() = _entitlements

	@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
	@JoinTable(name = "item_category", joinColumns = [JoinColumn(name = "item_id")], inverseJoinColumns = [JoinColumn(name = "cat_id")])
	private val _entitlements: MutableSet<EntitlementData> = mutableSetOf()

	fun add(entitlement: EntitlementData) {
		_entitlements.add(entitlement)
		entitlement.add(this)
	}

	fun remove(entitlement: EntitlementData) {
		_entitlements.remove(entitlement)
		entitlement.remove(this)
	}
}
