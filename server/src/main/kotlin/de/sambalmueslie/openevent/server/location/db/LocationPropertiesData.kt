package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.location.api.LocationProperties
import de.sambalmueslie.openevent.server.location.api.LocationPropertiesChangeRequest
import javax.persistence.*

@Entity(name = "LocationProperties")
@Table(name = "location_properties")
data class LocationPropertiesData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column
	var size: Int = 0
) : DataObject<LocationProperties> {

	companion object {
		fun convert(properties: LocationPropertiesChangeRequest) = LocationPropertiesData(0L, properties.size)
	}

	override fun convert() = LocationProperties(id, size)
}
