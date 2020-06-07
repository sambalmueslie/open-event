package de.sambalmueslie.oevent.logic.location


import de.sambalmueslie.oevent.logic.common.BusinessObjectService
import de.sambalmueslie.oevent.logic.common.findByIdOrNull
import de.sambalmueslie.oevent.logic.location.api.Address
import de.sambalmueslie.oevent.logic.location.api.GeoLocation
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.logic.location.api.LocationChangeRequest
import de.sambalmueslie.oevent.logic.location.db.AddressData
import de.sambalmueslie.oevent.logic.location.db.GeoLocationData
import de.sambalmueslie.oevent.logic.location.db.LocationData
import de.sambalmueslie.oevent.logic.location.db.LocationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LocationService(private val repository: LocationRepository) : BusinessObjectService<Location, LocationChangeRequest> {

	private val validator = LocationValidator()

	companion object {
		val logger: Logger = LoggerFactory.getLogger(LocationService::class.java)
	}

	override fun getAll(): List<Location> {
		return repository.findAll().map { it.convert() }
	}

	override fun get(id: Long): Location? {
		return repository.findByIdOrNull(id)?.convert()
	}

	override fun create(request: LocationChangeRequest): Location {
		return persist(convert(request)).convert()
	}

	override fun update(id: Long, request: LocationChangeRequest): Location {
		val existing = repository.findByIdOrNull(id) ?: return create(request)
		merge(existing, request)
		return persist(existing).convert()
	}

	private fun merge(data: LocationData, request: LocationChangeRequest) {
		merge(data.addressData, request.address)
		merge(data.geoLocation, request.geoLocation)
		data.size = request.size
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

	override fun delete(id: Long) {
		repository.deleteById(id)
	}


	private fun convert(request: LocationChangeRequest): LocationData {
		return LocationData(0, convert(request.address), convert(request.geoLocation), request.size)
	}

	private fun convert(address: Address): AddressData {
		return AddressData(0, address.street, address.streetNumber, address.zip, address.city, address.country, address.additionalInfo)
	}

	private fun convert(geo: GeoLocation): GeoLocationData {
		return GeoLocationData(0, geo.lat, geo.lon)
	}

	private fun persist(data: LocationData): LocationData {
		validator.validate(data)
		return if (data.id == 0L) {
			repository.save(data)
		} else {
			repository.update(data)
		}
	}


}
