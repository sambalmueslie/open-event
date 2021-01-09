package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.location.api.*
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class StructureCrudServiceTest(
	userRepo: UserRepository,
	private val service: StructureCrudService
) {
	private val user: UserData = UserUtils.getUser(userRepo)
	private val title = "Test title"
	private val shortText = "Test short text"
	private val longText = "Test long text"
	private val imageUrl = "Test image url"
	private val iconUrl = "Test icon url"

	@Test
	fun `create new structure without location`() {
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val request = StructureChangeRequest(item, null, null)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
		val owner = user.convert()
		val description = ItemDescription(2L, title, shortText, longText, imageUrl, iconUrl)
		assertEquals(Structure(0L, true, true, true, owner, description, null), result)
	}

	@Test
	fun `create new event with location`() {
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)

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
		val request = StructureChangeRequest(item, locationRequest, null)

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
		assertEquals(Structure(0L, true, true, true, owner, description, location), result)

	}
}
