package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.location.api.Location
import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var addressData: AddressData = AddressData(),
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var geoLocation: GeoLocationData = GeoLocationData(),
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var properties: LocationPropertiesData = LocationPropertiesData()
) : DataObject<Location> {
	companion object {
		fun convert(addressData: AddressData, geoLocationData: GeoLocationData, propertiesData: LocationPropertiesData): LocationData {
			return LocationData(0L, addressData, geoLocationData, propertiesData)
		}
	}

	override fun convert() = Location(id, addressData.convert(), geoLocation.convert(), properties.convert())
}
