package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.location.api.GeoLocation
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationData(
		@Id
		var id: Long,
		@Column
		var lat: Double,
		@Column
		var lon: Double
) {
	fun convert(): GeoLocation {
		return GeoLocation(lat, lon)
	}
}
