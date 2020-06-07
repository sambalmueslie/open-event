package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.common.DataObject
import de.sambalmueslie.oevent.logic.location.api.Location
import de.sambalmueslie.openchurch.common.DataObject
import de.sambalmueslie.openchurch.logic.item.api.Item
import de.sambalmueslie.openchurch.logic.location.api.Address
import de.sambalmueslie.openchurch.logic.location.api.GeoLocation
import de.sambalmueslie.openchurch.logic.location.api.Location
import de.sambalmueslie.openchurch.logic.location.api.LocationProperties
import javax.persistence.*

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
		@Id
		val id: Long,
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		var addressData: AddressData,
		@OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
		var geoLocation: GeoLocationData,
		@Column
		var size: Int
) : DataObject<Location> {

	override fun convert(): Location {
		return Location(id, addressData.convert(), geoLocation.convert(), size)
	}

}
