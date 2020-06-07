package de.sambalmueslie.oevent.logic.item.db

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.logic.item.api.Item
import javax.persistence.*

@Entity(name = "Item")
@Table(name = "item")
data class ItemData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column
		override var title: String = "",
		@Column
		override var shortText: String = "",
		@Column(columnDefinition = "TEXT")
		override var longText: String = "",
		@Column
		override var imageUrl: String = "",
		@Column
		override var iconUrl: String = ""
) : DataObject<Item>, Item {

	override fun convert(context: DataObjectContext) = this
}
