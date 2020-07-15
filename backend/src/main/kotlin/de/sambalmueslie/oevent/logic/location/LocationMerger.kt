package de.sambalmueslie.oevent.logic.location


import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.common.DataObjectMerger
import de.sambalmueslie.oevent.logic.location.api.Address
import de.sambalmueslie.oevent.logic.location.api.GeoLocation
import de.sambalmueslie.oevent.logic.location.api.LocationChangeRequest
import de.sambalmueslie.oevent.logic.location.db.LocationEntity
import de.sambalmueslie.oevent.model.AddressData
import de.sambalmueslie.oevent.model.GeoLocationData

class LocationMerger : DataObjectMerger<LocationEntity, LocationChangeRequest> {

	override fun merge(existing: LocationEntity?, request: LocationChangeRequest, context: DataObjectContext): LocationEntity {
		val data = existing ?: LocationEntity()
		merge(data.addressData, request.address)
		merge(data.geoLocation, request.geoLocation)
		data.size = request.size
		return data
	}

	private fun merge(data: AddressData, address: Address) {
		data.additionalInfo = address.additionalInfo
		data.city = address.city
		data.country = address.country
		data.street = address.street
		data.streetNumber = address.streetNumber
		data.zip = address.zip
	}

	private fun merge(data: GeoLocationData, geo: GeoLocation) {
		data.lat = geo.lat
		data.lon = geo.lon
	}
}
