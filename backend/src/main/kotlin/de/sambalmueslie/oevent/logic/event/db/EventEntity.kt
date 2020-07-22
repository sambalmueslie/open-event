package de.sambalmueslie.oevent.logic.event.db

import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.logic.event.api.Event
import de.sambalmueslie.oevent.logic.item.db.ItemEntity
import de.sambalmueslie.oevent.logic.structure.api.Structure
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.persistence.*

@Entity(name = "Event")
@Table(name = "event")
data class EventEntity(
		@Column
		var start: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
		@Column
		var stop: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
		@Column
		override var title: String = "",
		@Column(columnDefinition = "TEXT")
		override var shortText: String = "",
		@Column(columnDefinition = "TEXT")
		override var longText: String = "",
		@Column
		override var imageUrl: String = "",
		@Column
		override var iconUrl: String = "",
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0
) : ItemEntity(id, title, shortText, longText, imageUrl, iconUrl), DataObject<Event> {

	override fun convert(context: DataObjectContext): Event {
		return Event(id, title, shortText, longText, imageUrl, iconUrl, start, stop)
	}
}
