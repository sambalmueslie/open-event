package de.sambalmueslie.openevent.server.item.db

import javax.persistence.*

@Entity(name = "ItemDescription")
@Table(name = "item_description")
data class ItemDescriptionData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
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
)
