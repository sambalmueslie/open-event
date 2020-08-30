package de.sambalmueslie.oevent.logic.item.db

import de.sambalmueslie.oevent.logic.item.api.Item
import javax.persistence.*

@Entity(name = "Item")
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class ItemEntity(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column
		override var title: String = "",
		@Column(columnDefinition = "TEXT")
		override var shortText: String = "",
		@Column(columnDefinition = "TEXT")
		override var longText: String = "",
		@Column
		override var imageUrl: String = "",
		@Column
		override var iconUrl: String = ""
) : Item {

}
