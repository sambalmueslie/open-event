package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.logic.category.api.Category
import javax.persistence.*


@Entity(name = "Category")
@Table(name = "category")
data class CategoryEntity(
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
}
