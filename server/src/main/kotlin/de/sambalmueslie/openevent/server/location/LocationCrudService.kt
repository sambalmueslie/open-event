package de.sambalmueslie.openevent.server.location


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
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

	override fun create(user: User, request: LocationChangeRequest): Location? {
		return createData(user, request).second
	}

	fun createData(user: User, request: LocationChangeRequest): Pair<LocationData, Location> {
		val address = addressRepository.save(AddressData.convert(request.address))
		val geoLocation = geoLocationRepository.save(GeoLocationData.convert(request.geoLocation))
		val properties = propertiesRepository.save(LocationPropertiesData.convert(request.properties))
		val data = repository.save(LocationData.convert(address, geoLocation, properties))
		val result = data.convert()
		notifyCommon(CommonChangeEvent(result, CommonChangeEventType.CREATED))
		return Pair(data, result)
	}

	override fun update(user: User, objId: Long, request: LocationChangeRequest): Location? {
		TODO("Not yet implemented")
	}


}
