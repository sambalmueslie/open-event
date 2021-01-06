package de.sambalmueslie.openevent.server.user.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.server.common.BusinessObject

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
        @JsonProperty("id")
        override val id: Long,
        @JsonProperty("externalId")
        val externalId: String = "",
        @JsonProperty("userName")
        val userName: String = "",
        @JsonProperty("firstName")
        val firstName: String = "",
        @JsonProperty("lastName")
        val lastName: String = "",
        @JsonProperty("email")
        val email: String = "",
        @JsonProperty("iconUrl")
        val iconUrl: String = "",
        @JsonProperty("serviceUser")
        val serviceUser: Boolean = false
) : BusinessObject {

	fun getTitle(): String {
		return when {
			firstName.isNotBlank() && lastName.isNotBlank() -> "$firstName $lastName"
			else -> userName
		}
	}
}
