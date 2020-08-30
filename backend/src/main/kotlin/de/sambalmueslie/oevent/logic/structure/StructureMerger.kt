package de.sambalmueslie.oevent.logic.structure


import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.item.ItemMerger
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import de.sambalmueslie.oevent.logic.structure.db.StructureEntity

class StructureMerger : DataObjectMerger<StructureEntity, StructureChangeRequest> {

	private val itemMerger = ItemMerger<StructureEntity>()

	override fun merge(existing: StructureEntity?, request: StructureChangeRequest, context: DataObjectContext): StructureEntity {
		val data = existing ?: StructureEntity()
		itemMerger.merge(data, request.item)
		data.root = request.parentStructureId == null
		return data
	}

}
