package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.location.api.LocationProperties
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "LocationProperties")
@Table(name = "location_properties")
data class LocationPropertiesData(
	@Id
	val id: Long = 0L,
	@Column
	val size: Int = 0
) {
	companion object {
		fun convert(properties: LocationProperties) = LocationPropertiesData(0L, properties.size)
	}
}
