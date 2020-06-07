package de.sambalmueslie.oevent.logic.item


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.logic.common.DataObjectValidator
import de.sambalmueslie.oevent.logic.item.db.ItemData

class ItemValidator : DataObjectValidator<ItemData> {
	override fun validate(data: ItemData) {
		if (data.shortText.isBlank()) throw InvalidRequestStateException("Short Text of item ${data.id} must not be blank.")
	}


}
