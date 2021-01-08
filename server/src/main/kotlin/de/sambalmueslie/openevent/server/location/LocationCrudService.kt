package de.sambalmueslie.openevent.server.location


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.server.location.db.*
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LocationCrudService(
	private val repository: LocationRepository,
	private val addressRepository: AddressRepository,
	private val geoLocationRepository: GeoLocationRepository,
	private val propertiesRepository: LocationPropertiesRepository
) :
	BaseCrudService<Location, LocationChangeRequest, LocationData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(LocationCrudService::class.java)
	}

	override fun create(user: User, request: LocationChangeRequest): Location {
		val address = addressRepository.save(AddressData.convert(request.address)).convert()
		val geoLocation = geoLocationRepository.save(GeoLocationData.convert(request.geoLocation)).convert()
		val properties = propertiesRepository.save(LocationPropertiesData.convert(request.properties)).convert()
		val data = repository.save(LocationData.convert(address, geoLocation, properties))
		val result = data.convert(LocationConvertContent(address, geoLocation, properties))
		notifyCreated(user, result)
		return result
	}


	override fun update(user: User, objId: Long, request: LocationChangeRequest): Location? {
		TODO("Not yet implemented")
	}

	override fun convert(data: LocationData): Location {
		val address = addressRepository.findByIdOrNull(data.addressId)
			?: throw IllegalArgumentException("Cannot find address by ${data.addressId}")

		val geoLocation = geoLocationRepository.findByIdOrNull(data.geoLocationId)
			?: throw IllegalArgumentException("Cannot find geo location by ${data.geoLocationId}")

		val properties = propertiesRepository.findByIdOrNull(data.propertiesId)
			?: throw IllegalArgumentException("Cannot find properties by ${data.propertiesId}")
		return data.convert(LocationConvertContent(address.convert(), geoLocation.convert(), properties.convert()))
	}


}
