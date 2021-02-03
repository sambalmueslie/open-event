package de.sambalmueslie.openevent.server.event

import de.sambalmueslie.openevent.server.auth.AuthUtils.Companion.getAuthToken
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.event.api.PeriodChangeRequest
import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.location.LocationUtil
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
internal class EventControllerTest(userRepository: UserRepository): BaseControllerTest(userRepository) {

	private val baseUrl = "/api/event"
	private val start = LocalDateTime.of(2020, 12, 1, 20, 15)
	private val end = LocalDateTime.of(2020, 12, 1, 22, 30)


	@Test
	fun `create, read update and delete - admin`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val period = PeriodChangeRequest(start, end)
		val createRequest = HttpRequest.POST(baseUrl, EventChangeRequest(item, period, null)).bearerAuth(adminToken)
		val createResult = client.toBlocking().exchange(createRequest, Event::class.java)
		assertEquals(HttpStatus.OK, createResult.status)
		val createEvent = createResult.body()!!
		val description = ItemDescriptionUtil.getCreateDescription(createEvent.description.id)
		val event = Event(createEvent.id, Period(period.start, period.end), admin, description, null, true)
		assertEquals(event, createEvent)

		val getRequest = HttpRequest.GET<String>("$baseUrl/${event.id}").bearerAuth(adminToken)
		val getResult = client.toBlocking().exchange(getRequest, Event::class.java)
		assertEquals(HttpStatus.OK, getResult.status)
		assertEquals(event, getResult.body())

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken)
		val getAllResult = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, Event::class.java))
		assertEquals(HttpStatus.OK, getAllResult.status)
		assertEquals(listOf(event), getAllResult.body()?.content)


		val locationRequest = LocationUtil.getCreateRequest()
		val updateRequest = HttpRequest.PUT("$baseUrl/${event.id}", EventChangeRequest(item, period, locationRequest)).bearerAuth(adminToken)
		val updateResult = client.toBlocking().exchange(updateRequest, Event::class.java)
		assertEquals(HttpStatus.OK, updateResult.status)
		val updateEvent = updateResult.body()!!
		val location = LocationUtil.getCreateLocation(updateEvent.location!!.id)
		assertEquals(Event(updateEvent.id, Period(period.start, period.end), admin, description, location, true), updateResult.body())

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/${event.id}").bearerAuth(adminToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken), Argument.of(Page::class.java, Event::class.java))
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Event>(), getAllEmptyResult.body()?.content)

	}
}
