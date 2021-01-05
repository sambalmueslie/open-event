package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.location.api.GeoLocation
import javax.persistence.*

@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	@Column
	val lat: Double = 0.0,
	@Column
	val lon: Double = 0.0
) {
	companion object {
		fun convert(geoLocation: GeoLocation) = GeoLocationData(0L, geoLocation.lat, geoLocation.lon)
	}
}

