package de.sambalmueslie.openevent.server.structure.db


import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.db.ItemDataObject
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity(name = "Structure")
@Table(name = "structure")
data class StructureData(
	@Id
	val id: Long = 0,
	@Column(nullable = false)
	override var ownerId: Long = 0L,
	@Column(nullable = false)
	override var descriptionId: Long = 0L,
	@Column(nullable = false)
	var locationId: Long? = null,
	@Column(nullable = false)
	var root: Boolean = true,
	@Column(nullable = false)
	var visible: Boolean = true,
	@Column(nullable = false)
	var autoAcceptViewer: Boolean = true,
) : ItemDataObject<Structure, StructureConvertContent> {

	companion object {
		fun convert(user: User, request: StructureChangeRequest, description: ItemDescription, location: Location? = null): StructureData {
			return StructureData(0L, user.id, description.id, location?.id, request.parentStructureId == null)
		}
	}

	override fun convert(content: StructureConvertContent): Structure {
		return Structure(id, root, visible, autoAcceptViewer, content.owner, content.description, content.location)
	}

	fun update(request: StructureChangeRequest, description: ItemDescription, location: Location?) {
		descriptionId = description.id
		locationId = location?.id
		root = request.parentStructureId == null
	}

}
