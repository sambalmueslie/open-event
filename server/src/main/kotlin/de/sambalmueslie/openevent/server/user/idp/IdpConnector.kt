package de.sambalmueslie.openevent.server.user.idp

import de.sambalmueslie.openevent.server.user.db.UserData

interface IdpConnector {
	fun read(externalId: String): UserData?
}
