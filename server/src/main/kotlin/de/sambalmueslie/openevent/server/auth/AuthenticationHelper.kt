package de.sambalmueslie.openevent.server.auth

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import de.sambalmueslie.openevent.server.config.IdpConfig
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import io.micronaut.security.authentication.Authentication
import javax.inject.Singleton

@Singleton
class AuthenticationHelper(private val config: IdpConfig) {

	fun checkForAdmin(authentication: Authentication) {
		if (!hasRole(authentication, config.adminRoleName)) throw InsufficientPermissionsException("")
	}

	fun checkForEntitlement(authentication: Authentication, entitlement: Entitlement): Boolean {
		return hasRole(authentication, entitlement.name)
	}

	private fun hasRole(authentication: Authentication, role: String): Boolean {
		val attributes = authentication.attributes
		val resourceAccess = attributes["resource_access"] as JSONObject? ?: return false
		val backend = resourceAccess[config.backendResource] as JSONObject? ?: return false
		val roles = backend["roles"] as JSONArray? ?: return false
		return roles.toArray().any { it.toString().equals(role, true) }
	}

}
