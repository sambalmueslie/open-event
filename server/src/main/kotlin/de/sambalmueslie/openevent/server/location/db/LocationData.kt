package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.location.api.LocationChangeRequest
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var addressData: AddressData = AddressData(),
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var geoLocation: GeoLocationData = GeoLocationData(),
	@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
	var properties: LocationPropertiesData = LocationPropertiesData()
) {
	companion object {
		fun create(request: LocationChangeRequest): LocationData {
			val addressData = AddressData.convert(request.address)
			val geoLocationData = GeoLocationData.convert(request.geoLocation)
			val propertiesData = LocationPropertiesData.convert(request.properties)
			return LocationData(0L, addressData, geoLocationData, propertiesData)
		}
	}
}
