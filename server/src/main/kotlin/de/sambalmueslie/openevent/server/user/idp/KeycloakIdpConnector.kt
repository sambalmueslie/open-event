package de.sambalmueslie.openevent.server.user.idp


import de.sambalmueslie.openevent.server.cache.CacheService
import de.sambalmueslie.openevent.server.config.IdpConfig
import de.sambalmueslie.openevent.server.user.api.User
import de.sambalmueslie.openevent.server.user.db.UserData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class KeycloakIdpConnector(
		cacheService: CacheService,
		private val config: IdpConfig
) : IdpConnector {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(KeycloakIdpConnector::class.java)
	}

	override fun read(externalId: String): UserData? {
		TODO("Not yet implemented")
	}

}
