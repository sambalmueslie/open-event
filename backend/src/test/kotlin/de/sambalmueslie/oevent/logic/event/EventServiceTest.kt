package de.sambalmueslie.oevent.logic.event

import de.sambalmueslie.oevent.logic.event.api.Event
import de.sambalmueslie.oevent.logic.event.api.EventChangeRequest
import de.sambalmueslie.oevent.logic.event.db.EventEntity
import de.sambalmueslie.oevent.logic.event.db.EventRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class EventServiceTest {

//
//	private val repository: EventRepository = mockk()
//	private val service = EventService(repository)
//
//
//	@Test
//	fun `create new event`() {
//		val start = LocalDateTime.of(2020, 7, 15, 20, 0, 0)
//		val end = start.plusHours(2)
//		val title: String = "Title"
//		val shortText: String = "Short Text"
//		val longText: String = "Long Text"
//		val imageUrl: String = "Image Url"
//		val iconUrl: String = "Icon Url"
//
//		val dataSlot = slot<EventEntity>()
//		every { repository.save(capture(dataSlot)) } answers { dataSlot.captured }
//
//		val request = EventChangeRequest(start, end, title, shortText, longText, imageUrl, iconUrl)
//		val result = service.create(request)
//		assertEquals(Event(0, title, shortText, longText, imageUrl, iconUrl, start, end), result)
//		assertEquals(EventEntity(start, end, title, shortText, longText, imageUrl, iconUrl, 0), dataSlot.captured)
//
//	}

}
