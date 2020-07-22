package de.sambalmueslie.oevent.logic.structure


import de.sambalmueslie.oevent.api.Address
import de.sambalmueslie.oevent.api.GeoLocation
import de.sambalmueslie.oevent.logic.item.api.ItemChangeRequest
import de.sambalmueslie.oevent.logic.location.api.AddressChangeRequest
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.logic.location.api.LocationChangeRequest
import de.sambalmueslie.oevent.logic.structure.api.Structure
import de.sambalmueslie.oevent.logic.structure.api.StructureChangeRequest
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import javax.inject.Inject

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@MicronautTest
internal class StructureServiceTest {

	companion object {
		private var idCtr = 0L
	}

	@Inject
	lateinit var service: StructureService

	@Test
	fun `crud structure without parent and location`() {
		val itemReq = ItemChangeRequest("Title", "Short Text", "Long Text", "Image Url", "Icon Url")
		val request = StructureChangeRequest(itemReq)
		idCtr++
		assertEquals(Structure(idCtr, true, itemReq.title, itemReq.shortText, itemReq.longText, itemReq.imageUrl, itemReq.iconUrl, null), service.create(request))

		assertEquals(Structure(idCtr, true, itemReq.title, itemReq.shortText, itemReq.longText, itemReq.imageUrl, itemReq.iconUrl, null), service.get(idCtr))

		val itemUpdate = ItemChangeRequest("New Title", "New Short Text", "New Long Text", "New Image Url", "New Icon Url")
		val update = StructureChangeRequest(itemUpdate)
		assertEquals(Structure(idCtr, true, itemUpdate.title, itemUpdate.shortText, itemUpdate.longText, itemUpdate.imageUrl, itemUpdate.iconUrl, null), service.update(idCtr, update))

		service.delete(idCtr)
		assertNull(service.get(idCtr))
	}

	@Test
	fun `crud structure without parent but location`() {
		val itemReq = ItemChangeRequest("Title", "Short Text", "Long Text", "Image Url", "Icon Url")
		val addrReq = AddressChangeRequest("Street", "4", "Zip", "City", "Country", "AdditionalInfo")
		val locReq = LocationChangeRequest(addrReq)
		val request = StructureChangeRequest(itemReq, locReq)
		idCtr++
		val location = Location(1, Address(1, addrReq.street, addrReq.streetNumber, addrReq.zip, addrReq.city, addrReq.country, addrReq.additionalInfo),
				GeoLocation(1, 0.0, 0.0), 0)
		assertEquals(Structure(idCtr, true, itemReq.title, itemReq.shortText, itemReq.longText, itemReq.imageUrl, itemReq.iconUrl, location), service.create(request))
	}

	@Test
	fun `crud structure with parent but without location`() {
		val itemReq = ItemChangeRequest("Title", "Short Text", "Long Text", "Image Url", "Icon Url")
		val request = StructureChangeRequest(itemReq)
		idCtr++
		assertEquals(Structure(idCtr, true, itemReq.title, itemReq.shortText, itemReq.longText, itemReq.imageUrl, itemReq.iconUrl, null), service.create(request))

		val childItem = ItemChangeRequest("Child Title", "Child Short Text", "Child Long Text", "Child Image Url", "Child Icon Url")
		val childReq = StructureChangeRequest(childItem, null, idCtr)
		idCtr++
		assertEquals(Structure(idCtr, false, childItem.title, childItem.shortText, childItem.longText, childItem.imageUrl, childItem.iconUrl, null), service.create(childReq))
	}
}
