package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.location.api.Address
import de.sambalmueslie.openevent.server.location.api.GeoLocation
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.location.api.LocationProperties
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column(nullable = false)
	var addressId: Long = 0L,
	@Column(nullable = false)
	var geoLocationId: Long = 0L,
	@Column(nullable = false)
	var propertiesId: Long = 0L,
) : DataObject<Location, LocationConvertContent> {
	companion object {
		fun convert(address: Address, geoLocation: GeoLocation, properties: LocationProperties): LocationData {
			return LocationData(0L, address.id, geoLocation.id, properties.id)
		}
	}

	override fun convert(content: LocationConvertContent) = Location(id, content.address, content.geoLocation, content.properties)

	fun update(address: Address, geoLocation: GeoLocation, properties: LocationProperties) {
		addressId = address.id
		geoLocationId = geoLocation.id
		propertiesId = properties.id
	}
}
