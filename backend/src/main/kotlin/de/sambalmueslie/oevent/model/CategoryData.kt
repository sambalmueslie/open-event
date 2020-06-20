package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.api.Category
import javax.persistence.*


@Entity(name = "Category")
@Table(name = "category")
data class CategoryData(
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false, unique = true)
		var name: String = "",
		@Column(nullable = false)
		var iconUrl: String = ""
) : DataObject<Category> {

	override fun convert(context: DataObjectContext): Category {
		return Category(id, name, iconUrl)
	}

	val items: Set<ItemData>
		get() = _items

	@ManyToMany(mappedBy = "_categories")
	private val _items: MutableSet<ItemData> = mutableSetOf()

	fun add(item: ItemData) {
		_items.add(item)
	}

	fun remove(item: ItemData) {
		_items.remove(item)
	}

}
