package de.sambalmueslie.openevent.server.user.idp


import de.sambalmueslie.openevent.server.cache.CacheService
import de.sambalmueslie.openevent.server.config.IdpConfig
import de.sambalmueslie.openevent.server.user.db.UserData
import io.micronaut.http.HttpAttributes
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class KeycloakIdpConnector(
	private val config: IdpConfig,
	@Client(value = "\${idp.base-url:`http://localhost`}", id = "keycloak") private val client: RxHttpClient
) : IdpConnector {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(KeycloakIdpConnector::class.java)
	}

	override fun read(externalId: String): UserData? {
		val url = "${config.baseUrl}/admin/realms/${config.realm}/users/$externalId"
		try {
			val request = HttpRequest.GET<Any>(url)
				.setAttribute(HttpAttributes.SERVICE_ID.toString(), "keycloak")
			val user = client.retrieve(request, UserRepresentation::class.java).retry(3).blockingFirst() ?: return null
			return convert(user)
		} catch (e: Exception) {
			logger.error("Cannot read user data for $externalId", e)
		}
		return null
	}


	private fun convert(user: UserRepresentation): UserData {
		return UserData(0, user.id?: "", user.firstName ?: "", user.lastName?: "", user.email?: "", "")
	}

}
