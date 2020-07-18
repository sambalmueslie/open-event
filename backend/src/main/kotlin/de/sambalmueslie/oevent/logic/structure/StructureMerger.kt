package de.sambalmueslie.oevent.logic.structure


import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import de.sambalmueslie.oevent.logic.structure.db.StructureEntity
import de.sambalmueslie.oevent.model.StructureData

class StructureMerger : DataObjectMerger<StructureEntity, StructureChangeRequest> {

	override fun merge(existing: StructureEntity?, request: StructureChangeRequest, context: DataObjectContext): StructureEntity {
		val data = existing ?: StructureEntity()
		data.root = request.parentStructureId == null
		return data
	}

}
