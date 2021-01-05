package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.item.db.ItemDescriptionData
import de.sambalmueslie.openevent.server.location.db.LocationData
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Event")
@Table(name = "event")
data class EventData(
	@Id
	val id: Long = 0L,
	@Column(name = "period_start")
	var start: LocalDateTime = LocalDateTime.now(),
	@Column(name = "period_end")
	var end: LocalDateTime = LocalDateTime.now(),
	@OneToOne
	var description: ItemDescriptionData,
	@OneToOne
	var location: LocationData? = null
) : DataObject<Event> {

	override fun convert(): Event {
		TODO("Not yet implemented")
	}
}
