package de.sambalmueslie.oevent.logic.structure.db

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.logic.item.db.ItemEntity
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.logic.structure.api.Structure
import javax.persistence.*

@Entity(name = "Structure")
@Table(name = "structure")
data class StructureEntity(
		@Column(nullable = false)
		var root: Boolean = true,
		@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
		@JoinTable(name = "struct_child",
				joinColumns = [JoinColumn(name = "fk_parent")],
				inverseJoinColumns = [JoinColumn(name = "fk_child")]
		)
		var children: MutableSet<StructureEntity> = mutableSetOf()
) : DataObject<Structure>, ItemEntity() {

	@ManyToMany(mappedBy = "children")
	var parent: MutableSet<StructureEntity> = mutableSetOf()

	override fun convert(context: DataObjectContext): Structure {
		val location = context.getDependency(Location::class)
		return Structure(id, root, title, shortText, longText, imageUrl, iconUrl, location)
	}
}

