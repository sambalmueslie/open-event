package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.category.api.Category
import de.sambalmueslie.oevent.logic.common.DataObject
import de.sambalmueslie.oevent.logic.common.DataObjectContext
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

	override fun convert(dependencies: DataObjectContext): Category {
		return Category(id, name, iconUrl)
	}

}
