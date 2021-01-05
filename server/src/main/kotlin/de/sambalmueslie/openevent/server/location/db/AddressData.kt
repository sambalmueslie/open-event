package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.location.api.Address
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Address")
@Table(name = "address")
data class AddressData(
	@Id
	val id: Long = 0L,
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
) {
	companion object {
		fun convert(address: Address) =
			AddressData(0L, address.street, address.streetNumber, address.zip, address.city, address.country, address.additionalInfo)
	}
}
