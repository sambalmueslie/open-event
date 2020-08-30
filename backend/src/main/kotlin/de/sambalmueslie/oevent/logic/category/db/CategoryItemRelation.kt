package de.sambalmueslie.oevent.logic.category.db

import de.sambalmueslie.oevent.logic.item.relation.ItemEntityRelation
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "CategoryItemRelation")
@Table(name = "category_item_relation")
data class CategoryItemRelation(
		@Id
		override var id: String = "",
		@Column(nullable = false)
		override val entityId: Long = 0,
		@Column(nullable = false)
		override val itemId: Long = 0
) : ItemEntityRelation
