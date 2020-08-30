package de.sambalmueslie.oevent.logic.category


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.model.CategoryData
import de.sambalmueslie.oevent.common.DataObjectValidator
import de.sambalmueslie.oevent.logic.category.db.CategoryEntity

class CategoryValidator : DataObjectValidator<CategoryEntity> {
	override fun validate(data: CategoryEntity) {
		if (data.name.isBlank()) throw InvalidRequestStateException("Name of category ${data.id} must not be blank.")
	}

}
