package de.sambalmueslie.openevent.server.location.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
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
	var street: String = "",
	@Column
	var streetNumber: String = "",
	@Column
	var zip: String = "",
	@Column
	var city: String = "",
	@Column
	var country: String = "",
	@Column
	var additionalInfo: String = ""
) : DataObject<Address, EmptyConvertContent> {

	companion object {
		fun convert(address: AddressChangeRequest) =
			AddressData(0L, address.street, address.streetNumber, address.zip, address.city, address.country, address.additionalInfo)
	}

	fun convert() = convert(EmptyConvertContent())
	override fun convert(content: EmptyConvertContent) = Address(id, street, streetNumber, zip, city, country, additionalInfo)


	fun update(request: AddressChangeRequest) {
		street = request.street
		streetNumber = request.streetNumber
		zip = request.zip
		city = request.city
		country = request.country
		additionalInfo = request.additionalInfo
	}
}
