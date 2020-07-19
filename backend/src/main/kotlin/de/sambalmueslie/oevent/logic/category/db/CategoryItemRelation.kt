package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.item.api.Item
import javax.persistence.*

@Entity(name = "CategoryItemRelation")
@Table(name = "category_item_relation")
data class CategoryItemRelation(
		@Id
		var id: String = "",
		@Column(nullable = false)
		val categoryId: Long = 0,
		@Column(nullable = false)
		val itemId: Long = 0
) {
	companion object {
		fun createId(item: Item, category: Category)  = "${item.id}#${category.id}"
	}
}
