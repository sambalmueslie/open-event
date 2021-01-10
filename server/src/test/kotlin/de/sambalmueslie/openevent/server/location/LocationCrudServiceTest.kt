package de.sambalmueslie.openevent.server.location

import de.sambalmueslie.openevent.server.location.api.*
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class LocationCrudServiceTest(
	userRepo: UserRepository,
	private val service: LocationCrudService
) {
	private val user: UserData = UserUtils.getUser(userRepo)
	private val street = "Test street"
	private val steetNumber = "Test street number"
	private val zip = "Test zip"
	private val city = "Test city"
	private val country = "Test country"
	private val additionalInfo = "Test additional info"
	private val size = 10

	@Test
	fun `create new location`() {
		val address = AddressChangeRequest(street, steetNumber, zip, city, country, additionalInfo)
		val geoLocation = GeoLocationChangeRequest()
		val properties = LocationPropertiesChangeRequest(size)
		val request = LocationChangeRequest(address, geoLocation, properties)
		val result = service.create(user.convert(), request)
		val location = Location(
			1L,
			Address(1L, street, steetNumber, zip, city, country, additionalInfo),
			GeoLocation(1L, 0.0, 0.0),
			LocationProperties(1L, size)
		)
		assertEquals(location, result)
	}


	@Test
	fun `update location - create, modify, delete`() {
		val address = AddressChangeRequest(street, steetNumber, zip, city, country, additionalInfo)
		val geoLocation = GeoLocationChangeRequest()
		val properties = LocationPropertiesChangeRequest(size)
		val request = LocationChangeRequest(address, geoLocation, properties)
		val result = service.update(user.convert(), null, request)
		assertNotNull(result)
		val location = Location(
			2L,
			Address(2L, street, steetNumber, zip, city, country, additionalInfo),
			GeoLocation(2L, 0.0, 0.0),
			LocationProperties(2L, size)
		)
		assertEquals(location, result)

		val updateAddress = AddressChangeRequest("$street update", steetNumber, zip, city, country, additionalInfo)
		val updateGeoLocation = GeoLocationChangeRequest(1.0, 2.0)
		val updateProperties = LocationPropertiesChangeRequest(size + 5)
		val updateRequest = LocationChangeRequest(updateAddress, updateGeoLocation, updateProperties)
		val updateResult = service.update(user.convert(), location.id, updateRequest)
		assertNotNull(updateResult)

		val updateLocation = Location(
			2L,
			Address(2L, updateAddress.street, steetNumber, zip, city, country, additionalInfo),
			GeoLocation(2L, updateGeoLocation.lat, updateGeoLocation.lon),
			LocationProperties(2L, updateProperties.size)
		)
		assertEquals(updateLocation, updateResult)

		val deleteResult = service.update(user.convert(), location.id, null)
		assertNull(deleteResult)

		val ignoreResult = service.update(user.convert(), null, null)
		assertNull(ignoreResult)
	}


}
