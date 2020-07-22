package de.sambalmueslie.oevent.logic.event

import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.event.api.EventChangeRequest
import de.sambalmueslie.oevent.logic.event.db.EventEntity
import de.sambalmueslie.oevent.logic.item.ItemMerger

class EventMerger : DataObjectMerger<EventEntity, EventChangeRequest> {

	private val itemMerger = ItemMerger<EventEntity>()
	override fun merge(existing: EventEntity?, request: EventChangeRequest, context: DataObjectContext): EventEntity {
		val data = existing ?: EventEntity()
		itemMerger.merge(data, request.item)
		data.start = request.start
		data.stop = request.stop
		return data
	}

}
