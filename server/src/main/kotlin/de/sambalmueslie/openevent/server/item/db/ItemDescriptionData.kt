package de.sambalmueslie.openevent.server.item.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import javax.persistence.*

@Entity(name = "ItemDescription")
@Table(name = "item_description")
data class ItemDescriptionData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,
	@Column
	var title: String = "",
	@Column
	var shortText: String = "",
	@Column(columnDefinition = "TEXT")
	var longText: String = "",
	@Column
	var imageUrl: String = "",
	@Column
	var iconUrl: String = ""
) : DataObject<ItemDescription> {

	companion object {
		fun convert(request: ItemDescriptionChangeRequest) = ItemDescriptionData(0L, request.title, request.shortText, request.longText, request.imageUrl, request.iconUrl)
	}

	override fun convert() = ItemDescription(id, title, shortText, longText, imageUrl, iconUrl)
}
