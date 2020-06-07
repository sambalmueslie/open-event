package de.sambalmueslie.oevent.logic.structure.db


import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.logic.structure.api.Structure
import javax.persistence.*


@Entity(name = "Structure")
@Table(name = "structure")
data class StructureData(
		@Id
		override var id: Long = 0,
		@Column(nullable = false)
		var root: Boolean = true,
		@ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
		@JoinTable(name = "struct_child",
				joinColumns = [JoinColumn(name = "fk_parent")],
				inverseJoinColumns = [JoinColumn(name = "fk_child")]
		)
		var children: MutableSet<StructureData> = mutableSetOf()
) : DataObject<Structure> {

	@ManyToMany(mappedBy = "children")
	var parent: MutableSet<StructureData> = mutableSetOf()

	override fun convert(context: DataObjectContext): Structure {
		return Structure(id, root)
	}
}
