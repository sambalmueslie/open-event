package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.common.DataObject
import javax.persistence.*


@Entity(name = "Category")
@Table(name = "category")
data class CategoryData(
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
		var id: Long,
		@Column(nullable = false, unique = true)
		var name: String,
		@Column(nullable = false)
		var iconUrl: String
) : DataObject<Category> {
	override fun convert(): Category {
		return Category(id, name, iconUrl)
	}
}
