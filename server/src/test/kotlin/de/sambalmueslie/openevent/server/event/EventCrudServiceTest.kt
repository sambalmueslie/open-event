package de.sambalmueslie.openevent.server.event

import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.EventChangeRequest
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.event.api.PeriodChangeRequest
import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.location.LocationUtil
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

	private val user: UserData = UserUtils.getFirstUser(userRepo)
	private val start = LocalDateTime.of(2020, 12, 1, 20, 15)
	private val end = LocalDateTime.of(2020, 12, 1, 22, 30)

	@Test
	fun `create new event without location`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val period = PeriodChangeRequest(start, end)
		val request = EventChangeRequest(item, period, null)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
		val owner = user.convert()
		val description = ItemDescriptionUtil.getCreateDescription(result!!.description.id)
		assertEquals(Event(result.id, Period(period.start, period.end), owner, description, null, true), result)

		val entitlement = entitlementService.findByUserIdAndItemIdAndType(user.id, result.id, ItemType.EVENT)
		assertEquals(ItemEntitlementEntry(entitlement.id, user.id, result.id, ItemType.EVENT, Entitlement.EDITOR), entitlement)
	}

	@Test
	fun `create new event with location`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val period = PeriodChangeRequest(start, end)
		val locationRequest = LocationUtil.getCreateRequest()
		val request = EventChangeRequest(item, period, locationRequest)

		val result = service.create(user.convert(), request)
		assertNotNull(result)

		val owner = user.convert()
		val description = ItemDescriptionUtil.getCreateDescription(result!!.description.id)
		val location = LocationUtil.getCreateLocation(result.location!!.id)
		assertEquals(Event(result.id, Period(period.start, period.end), owner, description, location, true), result)
	}
}
