package de.sambalmueslie.openevent.server.location

import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.location.api.*


class LocationUtil {

	companion object {
		private val createStreet = "Test street"
		private val updateStreet = "Test street - update"
		private val steetNumber = "Test street number"
		private val zip = "Test zip"
		private val city = "Test city"
		private val country = "Test country"
		private val additionalInfo = "Test additional info"
		private val createSize = 10
		private val updateSize = 20
		private val createLatitude = 1.0
		private val updateLatitude = 2.0

		fun getCreateRequest(): LocationChangeRequest {
			val address = AddressChangeRequest(createStreet, steetNumber, zip, city, country, additionalInfo)
			val geoLocation = GeoLocationChangeRequest(createLatitude)
			val properties = LocationPropertiesChangeRequest(createSize)
			return LocationChangeRequest(address, geoLocation, properties)
		}

		fun getUpdateRequest(): LocationChangeRequest {
			val address = AddressChangeRequest(updateStreet, steetNumber, zip, city, country, additionalInfo)
			val geoLocation = GeoLocationChangeRequest(updateLatitude)
			val properties = LocationPropertiesChangeRequest(updateSize)
			return LocationChangeRequest(address, geoLocation, properties)
		}

		fun getCreateLocation(objId: Long): Location {
			return Location(
				objId,
				Address(objId, createStreet, steetNumber, zip, city, country, additionalInfo),
				GeoLocation(objId, createLatitude),
				LocationProperties(objId, createSize)
			)
		}

		fun getUpdateLocation(objId: Long): Location {
			return Location(
				objId,
				Address(objId, updateStreet, steetNumber, zip, city, country, additionalInfo),
				GeoLocation(objId, updateLatitude),
				LocationProperties(objId, updateSize)
			)
		}
	}


}
