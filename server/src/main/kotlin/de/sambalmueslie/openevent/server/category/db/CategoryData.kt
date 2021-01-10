package de.sambalmueslie.openevent.server.category.db

import de.sambalmueslie.openevent.server.category.api.Category
import de.sambalmueslie.openevent.server.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
import javax.persistence.*

@Entity(name = "Category")
@Table(name = "category")
data class CategoryData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column(nullable = false)
	var name: String = "",
	@Column(nullable = false)
	var iconUrl: String = ""
) : DataObject<Category, EmptyConvertContent> {

	companion object {
		fun convert(request: CategoryChangeRequest) = CategoryData(0L, request.name, request.iconUrl)
	}

	fun convert() = convert(EmptyConvertContent())
	override fun convert(content: EmptyConvertContent): Category {
		return Category(id, name, iconUrl)
	}

	fun update(request: CategoryChangeRequest) {
		name = request.name
		iconUrl = request.iconUrl
	}
}
