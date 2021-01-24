package de.sambalmueslie.openevent.server.user


import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.idp.IdpConnector
import de.sambalmueslie.openevent.server.user.idp.KeycloakIdpConnector
import io.micronaut.context.annotation.Replaces
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Replaces(KeycloakIdpConnector::class)
@Singleton
class MockIdpConnector : IdpConnector {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(MockIdpConnector::class.java)
	}

	override fun read(externalId: String): UserData? {
		return UserUtils.createFirstSampleUser()
	}


}
