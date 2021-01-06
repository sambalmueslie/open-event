package de.sambalmueslie.oevent.logic.event

import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.common.DataObjectValidator
import de.sambalmueslie.oevent.logic.event.db.EventEntity

class EventValidator : DataObjectValidator<EventEntity> {
	override fun validate(data: EventEntity) {
		if (data.shortText.isBlank()) throw InvalidRequestStateException("Short Text of event ${data.id} must not be blank.")
	}
}
