package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.location.api.AddressChangeRequest
import de.sambalmueslie.openevent.server.location.api.GeoLocationChangeRequest
import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.server.location.api.LocationPropertiesChangeRequest
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
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

		val location = LocationChangeRequest(address, geoLocation, properties)
		val request = StructureChangeRequest(item, location, null)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
	}
}
