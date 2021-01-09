package de.sambalmueslie.openevent.server.event

import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.event.api.PeriodChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.location.api.*
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.time.LocalDateTime

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class EventCrudServiceTest(
	userRepo: UserRepository,
	private val service: EventCrudService,
	private val entitlementService: ItemEntitlementCrudService
) {

	private val user: UserData = UserUtils.getUser(userRepo)
	private val title = "Test title"
	private val shortText = "Test short text"
	private val longText = "Test long text"
	private val imageUrl = "Test image url"
	private val iconUrl = "Test icon url"
	private val start = LocalDateTime.of(2020, 12, 1, 20, 15)
	private val end = LocalDateTime.of(2020, 12, 1, 22, 30)

	@Test
	fun `create new event without location`() {
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val period = PeriodChangeRequest(start, end)
		val request = EventChangeRequest(item, period, null)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
		val owner = user.convert()
		val description = ItemDescription(2L, title, shortText, longText, imageUrl, iconUrl)
		assertEquals(Event(0L, Period(period.start, period.end), owner, description, null, true), result)

		val entitlement =entitlementService.findByUserIdAndItemIdAndType(user.id, result!!.id, ItemType.EVENT)
		assertEquals(ItemEntitlementEntry(2L, user.id, result.id, ItemType.EVENT, Entitlement.EDITOR), entitlement)
	}

	@Test
	fun `create new event with location`() {
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val period = PeriodChangeRequest(start, end)

		val street = "Test street"
		val steetNumber = "Test street number"
		val zip = "Test zip"
		val city = "Test city"
		val country = "Test country"
		val additionalInfo = "Test additional info"
		val address = AddressChangeRequest(street, steetNumber, zip, city, country, additionalInfo)

		val geoLocation = GeoLocationChangeRequest()
		val size = 10
		val properties = LocationPropertiesChangeRequest(size)

		val locationRequest = LocationChangeRequest(address, geoLocation, properties)
		val request = EventChangeRequest(item, period, locationRequest)

		val result = service.create(user.convert(), request)
		assertNotNull(result)

		val owner = user.convert()
		val description = ItemDescription(1L, title, shortText, longText, imageUrl, iconUrl)
		val location = Location(
			1L,
			Address(1L, street, steetNumber, zip, city, country, additionalInfo),
			GeoLocation(1L, 0.0, 0.0),
			LocationProperties(1L, size)
		)
		assertEquals(Event(0L, Period(period.start, period.end), owner, description, location, true), result)
	}
}
