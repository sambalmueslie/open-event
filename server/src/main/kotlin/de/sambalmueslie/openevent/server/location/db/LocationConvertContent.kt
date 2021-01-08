package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.ConvertContent
import de.sambalmueslie.openevent.server.location.api.Address
import de.sambalmueslie.openevent.server.location.api.GeoLocation
import de.sambalmueslie.openevent.server.location.api.LocationProperties

data class LocationConvertContent(
	val address: Address,
	val geoLocation: GeoLocation,
	val properties: LocationProperties
) : ConvertContent
