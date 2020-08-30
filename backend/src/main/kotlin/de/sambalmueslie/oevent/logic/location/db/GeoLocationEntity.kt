package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.api.GeoLocation
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationEntity(
		@Id
		var id: Long = 0,
		@Column
		var lat: Double = 0.0,
		@Column
		var lon: Double = 0.0
) {
	fun convert(): GeoLocation {
		return GeoLocation(id, lat, lon)
	}
}
