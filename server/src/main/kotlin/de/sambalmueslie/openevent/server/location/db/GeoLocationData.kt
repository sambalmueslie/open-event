package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
import de.sambalmueslie.openevent.server.location.api.GeoLocation
import de.sambalmueslie.openevent.server.location.api.GeoLocationChangeRequest
import javax.persistence.*

@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	@Column
	var lat: Double = 0.0,
	@Column
	var lon: Double = 0.0
) : DataObject<GeoLocation, EmptyConvertContent> {
	companion object {
		fun convert(geoLocation: GeoLocationChangeRequest) = GeoLocationData(0L, geoLocation.lat, geoLocation.lon)
	}

	fun convert() = convert(EmptyConvertContent())
	override fun convert(content: EmptyConvertContent) = GeoLocation(id, lat, lon)

	fun update(request: GeoLocationChangeRequest) {
		lat = request.lat
		lon = request.lon
	}
}

