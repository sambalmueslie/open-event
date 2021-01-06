package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.location.api.GeoLocation
import de.sambalmueslie.openevent.server.location.api.GeoLocationChangeRequest
import javax.persistence.*

@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column
	var lat: Double = 0.0,
	@Column
	var lon: Double = 0.0
) : DataObject<GeoLocation> {
	companion object {
		fun convert(geoLocation: GeoLocationChangeRequest) = GeoLocationData(0L, geoLocation.lat, geoLocation.lon)
	}

	override fun convert() = GeoLocation(id, lat, lon)
}

