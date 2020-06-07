package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.logic.location.api.Address
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "Address")
@Table(name = "address")
data class AddressData(
		@Id
		var id: Long,
		@Column
		var street: String,
		@Column
		var streetNumber: String,
		@Column
		var zip: String,
		@Column
		var city: String,
		@Column
		var country: String,
		@Column
		var additionalInfo: String
) {

	fun convert(): Address {
		return Address(street, streetNumber, zip, city, country, additionalInfo)
	}


}
