package de.sambalmueslie.openevent.server.event

import de.sambalmueslie.openevent.server.auth.AuthUtils.Companion.getAuthToken
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.event.api.PeriodChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.location.api.*
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
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
internal class EventControllerTest(userRepo: UserRepository) {
	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	private val baseUrl = "/api/event"
	private val user: UserData = UserUtils.getUser(userRepo)
	private val title = "Test title"
	private val shortText = "Test short text"
	private val longText = "Test long text"
	private val imageUrl = "Test image url"
	private val iconUrl = "Test icon url"
	private val start = LocalDateTime.of(2020, 12, 1, 20, 15)
	private val end = LocalDateTime.of(2020, 12, 1, 22, 30)
	private val street = "Test street"
	private val steetNumber = "Test street number"
	private val zip = "Test zip"
	private val city = "Test city"
	private val country = "Test country"
	private val additionalInfo = "Test additional info"

	@Test
	fun `create, read update and delete`() {
		val accessToken = getAuthToken(client)

		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val period = PeriodChangeRequest(start, end)
		val createRequest = HttpRequest.POST(baseUrl, EventChangeRequest(item, period, null)).bearerAuth(accessToken)
		val createResult = client.toBlocking().exchange(createRequest, Event::class.java)
		assertEquals(HttpStatus.OK, createResult.status)
		val owner = user.convert()
		val description = ItemDescription(1L, title, shortText, longText, imageUrl, iconUrl)
		val event = Event(0L, Period(period.start, period.end), owner, description, null, true)
		assertEquals(event, createResult.body())

		val getRequest = HttpRequest.GET<String>("$baseUrl/${event.id}").bearerAuth(accessToken)
		val getResult = client.toBlocking().exchange(getRequest, Event::class.java)
		assertEquals(HttpStatus.OK, getResult.status)
		assertEquals(event, getResult.body())

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken)
		val getAllResult = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, Event::class.java))
		assertEquals(HttpStatus.OK, getAllResult.status)
		assertEquals(listOf(event), getAllResult.body()?.content)


		val address = AddressChangeRequest(street, steetNumber, zip, city, country, additionalInfo)
		val geoLocation = GeoLocationChangeRequest()
		val size = 10
		val properties = LocationPropertiesChangeRequest(size)
		val locationRequest = LocationChangeRequest(address, geoLocation, properties)

		val updateRequest = HttpRequest.PUT("$baseUrl/${event.id}", EventChangeRequest(item, period, locationRequest)).bearerAuth(accessToken)
		val updateResult = client.toBlocking().exchange(updateRequest, Event::class.java)
		assertEquals(HttpStatus.OK, updateResult.status)
		val location = Location(
			1L,
			Address(1L, street, steetNumber, zip, city, country, additionalInfo),
			GeoLocation(1L, 0.0, 0.0),
			LocationProperties(1L, size)
		)
		assertEquals(Event(0L, Period(period.start, period.end), owner, description, location, true), updateResult.body())

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/${event.id}").bearerAuth(accessToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken), Argument.of(Page::class.java, Event::class.java))
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Event>(), getAllEmptyResult.body()?.content)

	}
}
