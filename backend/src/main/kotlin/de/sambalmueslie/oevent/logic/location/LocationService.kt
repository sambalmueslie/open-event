package de.sambalmueslie.oevent.logic.location


import de.sambalmueslie.oevent.logic.common.*
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
class LocationService(private val repository: LocationRepository) : BaseService<Location, LocationChangeRequest, LocationData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(LocationService::class.java)
	}

	private val validator = LocationValidator()
	override fun getValidator() = validator

	private  val merger = LocationMerger()
	override fun getMerger() = merger

}
