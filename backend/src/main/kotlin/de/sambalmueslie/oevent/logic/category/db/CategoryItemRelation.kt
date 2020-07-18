package de.sambalmueslie.oevent.logic.category.db

import javax.persistence.*

@Entity(name = "CategoryItemRelation")
@Table(name = "category_item_relation")
data class CategoryItemRelation(
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
		var id: Long = 0,
		@Column(nullable = false)
		val categoryId: Long = 0,
		@Column(nullable = false)
		val itemId: Long = 0
)
