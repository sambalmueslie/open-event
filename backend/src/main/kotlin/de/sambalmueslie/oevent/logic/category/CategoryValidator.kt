package de.sambalmueslie.oevent.logic.category


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.model.CategoryData
import de.sambalmueslie.oevent.common.DataObjectValidator

class CategoryValidator : DataObjectValidator<CategoryData> {
	override fun validate(data: CategoryData) {
		if (data.name.isBlank()) throw InvalidRequestStateException("Name of category ${data.id} must not be blank.")
	}

}
