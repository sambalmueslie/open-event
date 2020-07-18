package de.sambalmueslie.oevent.logic.structure


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.common.DataObjectValidator
import de.sambalmueslie.oevent.logic.structure.db.StructureEntity
import de.sambalmueslie.oevent.model.StructureData

class StructureValidator : DataObjectValidator<StructureEntity> {
	override fun validate(data: StructureEntity) {
		if (data.shortText.isBlank()) throw InvalidRequestStateException("Short Text of structure ${data.id} must not be blank.")
	}
}
