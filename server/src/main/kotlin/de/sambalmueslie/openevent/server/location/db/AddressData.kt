package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.location.api.Address
import de.sambalmueslie.openevent.server.location.api.AddressChangeRequest
import javax.persistence.*

@Entity(name = "Address")
@Table(name = "address")
data class AddressData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0L,
	@Column
	val street: String = "",
	@Column
	val streetNumber: String = "",
	@Column
	val zip: String = "",
	@Column
	val city: String = "",
	@Column
	val country: String = "",
	@Column
	val additionalInfo: String = ""
) : DataObject<Address> {
	companion object {
		fun convert(address: AddressChangeRequest) =
			AddressData(0L, address.street, address.streetNumber, address.zip, address.city, address.country, address.additionalInfo)
	}

	override fun convert() = Address(id, street, streetNumber, zip, city, country, additionalInfo)
}
