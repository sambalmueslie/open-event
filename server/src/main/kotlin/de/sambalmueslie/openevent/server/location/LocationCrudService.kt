package de.sambalmueslie.openevent.server.location


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.location.api.*
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


	override fun update(user: User, objId: Long, request: LocationChangeRequest): Location {
		val location = repository.findByIdOrNull(objId) ?: return create(user, request)
		return update(user, location, request)
	}

	override fun update(user: User, obj: LocationData, request: LocationChangeRequest): Location {
		val address = update(obj, request.address)
		val geoLocation = update(obj, request.geoLocation)
		val properties = update(obj, request.properties)
		obj.update(address, geoLocation, properties)
		val data = repository.update(obj)
		val result = data.convert(LocationConvertContent(address, geoLocation, properties))
		notifyUpdated(user, result)
		return result
	}


	private fun update(location: LocationData, request: AddressChangeRequest): Address {
		val address = addressRepository.findByIdOrNull(location.addressId)
		return if (address == null) {
			addressRepository.save(AddressData.convert(request)).convert()
		} else {
			address.update(request)
			addressRepository.update(address).convert()
		}
	}


	private fun update(location: LocationData, request: GeoLocationChangeRequest): GeoLocation {
		val geoLocation = geoLocationRepository.findByIdOrNull(location.geoLocationId)
		return if (geoLocation == null) {
			geoLocationRepository.save(GeoLocationData.convert(request)).convert()
		} else {
			geoLocation.update(request)
			geoLocationRepository.update(geoLocation).convert()
		}
	}

	private fun update(location: LocationData, request: LocationPropertiesChangeRequest): LocationProperties {
		val properties = propertiesRepository.findByIdOrNull(location.propertiesId)
		return if (properties == null) {
			propertiesRepository.save(LocationPropertiesData.convert(request)).convert()
		} else {
			properties.update(request)
			propertiesRepository.update(properties).convert()
		}
	}

	fun update(user: User, objId: Long?, request: LocationChangeRequest?): Location? {
		if (objId == null && request == null) return null

		val deletedAndExisting = objId != null && request == null
		if (deletedAndExisting) {
			delete(user, objId!!)
			return null
		}

		val createdAndNotExisting = objId == null && request != null
		if (createdAndNotExisting) {
			return create(user, request!!)
		}
		return update(user, objId!!, request!!)
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
