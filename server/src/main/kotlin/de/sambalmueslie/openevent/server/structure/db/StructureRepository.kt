package de.sambalmueslie.openevent.server.structure.db

import de.sambalmueslie.openevent.server.item.db.ItemRepository

interface StructureRepository : ItemRepository<StructureData> {
	fun findByParentStructureId(parentStructureId: Long): List<StructureData>
}
