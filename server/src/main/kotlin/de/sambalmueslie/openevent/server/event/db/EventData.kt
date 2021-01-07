package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescription
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
	@Column(nullable = false)
	var descriptionId: Long = 0L,
	@OneToOne
	var location: LocationData? = null
) : DataObject<Event> {

	companion object {
		fun convert(user: User, request: EventChangeRequest, description: ItemDescription, location: LocationData? = null): EventData {
			return EventData(0L, request.period.start, request.period.end, UserData.convert(user), description.id, location)
		}
	}

	override fun convert() = TODO("not implemented yet")//  = Event(id, Period(start, end), owner.convert(), description.convert(), location?.convert())
}
