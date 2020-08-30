package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.item.api.Item
import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelation
import de.sambalmueslie.oevent.logic.location.api.Location
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "LocationItemRelation")
@Table(name = "location_item_relation")
data class LocationItemRelation(
		@Id
		override var id: String = "",
		@Column(nullable = false)
		override val entityId: Long = 0,
		@Column(nullable = false)
		override val itemId: Long = 0
) : ItemEntityRelation
