package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.common.DataObject
import de.sambalmueslie.oevent.logic.common.DataObjectContext
import de.sambalmueslie.oevent.logic.location.api.Location
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
		@Id
		override var id: Long = 0,
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		var addressData: AddressData = AddressData(),
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		var geoLocation: GeoLocationData = GeoLocationData(),
		@Column
		var size: Int = -1
) : DataObject<Location> {

	override fun convert(dependencies: DataObjectContext): Location {
		return Location(id, addressData.convert(), geoLocation.convert(), size)
	}

}
