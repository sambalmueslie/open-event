package de.sambalmueslie.openevent.server.event.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.db.ItemDataObject
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.user.api.User
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Event")
@Table(name = "event")
data class EventData(
	@Id
	var id: Long = 0L,
	@Column(name = "period_start")
	var start: LocalDateTime = LocalDateTime.now(),
	@Column(name = "period_end")
	var end: LocalDateTime = LocalDateTime.now(),
	@Column(nullable = false)
	override var ownerId: Long = 0L,
	@Column(nullable = false)
	override var descriptionId: Long = 0L,
	@Column(nullable = false)
	var locationId: Long? = null,
	@Column
	var published: Boolean = false
) : ItemDataObject<Event, EventConvertContent> {

	companion object {
		fun convert(user: User, request: EventChangeRequest, description: ItemDescription, location: Location? = null): EventData {
			return EventData(0L, request.period.start, request.period.end, user.id, description.id, location?.id)
		}
	}

	override fun convert(content: EventConvertContent) = Event(id, Period(start, end), content.owner, content.description, content.location, published)
}
