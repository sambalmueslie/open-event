package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import de.sambalmueslie.oevent.model.AddressData
import de.sambalmueslie.oevent.model.GeoLocationData
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationEntity(
		@Id
		override var id: Long = 0,
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		@MapsId
		var addressData: AddressData = AddressData(),
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		@MapsId
		var geoLocation: GeoLocationData = GeoLocationData(),
		@Column
		var size: Int = -1
): DataObject<Location> {

	override fun convert(context: DataObjectContext): Location {
		return Location(id, addressData.convert(), geoLocation.convert(), size)
	}
}
