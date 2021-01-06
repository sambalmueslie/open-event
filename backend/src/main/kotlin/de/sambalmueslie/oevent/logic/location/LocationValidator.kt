package de.sambalmueslie.oevent.logic.location


import com.sun.jdi.request.InvalidRequestStateException
import de.sambalmueslie.oevent.common.DataObjectValidator
import de.sambalmueslie.oevent.logic.location.db.AddressEntity
import de.sambalmueslie.oevent.logic.location.db.GeoLocationEntity
import de.sambalmueslie.oevent.logic.location.db.LocationEntity

class LocationValidator : DataObjectValidator<LocationEntity> {

	override fun validate(data: LocationEntity) {
		validate(data.address)
		validate(data.geoLocation)
	}

	private fun validate(data: AddressEntity) {
		if (data.street.isBlank()) throw InvalidRequestStateException("Street of address ${data.id} must not be blank.")
		if (data.streetNumber.isBlank()) throw InvalidRequestStateException("Street number of address ${data.id} must not be blank.")
		if (data.zip.isBlank()) throw InvalidRequestStateException("Zip of address ${data.id} must not be blank.")
		if (data.city.isBlank()) throw InvalidRequestStateException("City of address ${data.id} must not be blank.")
	}

	private fun validate(data: GeoLocationEntity) {
		// intentionally left empty
	}

}
