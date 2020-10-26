package de.sambalmueslie.openevent.server.auth

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import io.micronaut.security.authentication.Authentication
import javax.inject.Singleton

@Singleton
class AuthenticationHelper {
	fun checkForAdmin(authentication: Authentication) {
		val attributes = authentication.attributes
		val resourceAccess = attributes["resource_access"] as JSONObject? ?: throw InsufficientPermissionsException("")
		val backend = resourceAccess["open-church-backend"] as JSONObject? ?: throw InsufficientPermissionsException("")
		val roles = backend["roles"] as JSONArray? ?: throw InsufficientPermissionsException("")
		roles.toArray().find { it == "ADMINISTRATOR" } ?: throw InsufficientPermissionsException("")
	}

}
