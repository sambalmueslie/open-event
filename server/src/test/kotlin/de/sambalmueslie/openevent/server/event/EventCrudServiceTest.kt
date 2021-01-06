package de.sambalmueslie.openevent.server.event

import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.PeriodChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
internal class EventCrudServiceTest(
	private val userRepo: UserRepository,
	private val service: EventCrudService
) {

	private val user: UserData

	init {
		val userId = "Test user id"
		val userName = "Test user name"
		user = userRepo.save(UserData(0L, userId, userName, "", "", "", "", LocalDateTime.now(), false))
	}

	@Test
	fun `create new event without location`() {
		val title = "Test title"
		val shortText = "Test short text"
		val longText = "Test long text"
		val imageUrl = "Test image url"
		val iconUrl = "Test icon url"
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val start = LocalDateTime.of(2020, 12, 1, 20, 15)
		val end = LocalDateTime.of(2020, 12, 1, 22, 30)
		val period = PeriodChangeRequest(start, end)
		val request = EventChangeRequest(item, period, null)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
	}

}
