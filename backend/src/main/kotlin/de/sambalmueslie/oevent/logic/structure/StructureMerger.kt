package de.sambalmueslie.oevent.logic.structure


import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import de.sambalmueslie.oevent.model.StructureData

class StructureMerger : DataObjectMerger<StructureData, StructureChangeRequest> {

	override fun merge(existing: StructureData?, request: StructureChangeRequest, context: DataObjectContext): StructureData {
		val data = existing ?: StructureData()
		data.root = request.parentStructureId == null
		return data
	}

}
