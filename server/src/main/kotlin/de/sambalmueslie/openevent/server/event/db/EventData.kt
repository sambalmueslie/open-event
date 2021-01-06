package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.item.db.ItemDescriptionData
import de.sambalmueslie.openevent.server.location.db.LocationData
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.db.UserData
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Event")
@Table(name = "event")
data class EventData(
	@Id
	var id: Long = 0L,
	@Column(name = "period_start")
	var start: LocalDateTime = LocalDateTime.now(),
	@Column(name = "period_end")
	var end: LocalDateTime = LocalDateTime.now(),
	@OneToOne
	var owner: UserData = UserData(),
	@OneToOne
	var description: ItemDescriptionData = ItemDescriptionData(),
	@OneToOne
	var location: LocationData? = null
) : DataObject<Event> {

	companion object {
		fun convert(user: User, request: EventChangeRequest, description: ItemDescriptionData): EventData {
			val location = request.location?.let { LocationData.convert(it) }
			return EventData(0L, request.period.start, request.period.end, UserData.convert(user), description, location)
		}
	}

	override fun convert() = Event(id, Period(start, end), owner.convert(), description.convert(), location?.convert())
}
