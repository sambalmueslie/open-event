package de.sambalmueslie.openevent.test


import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import org.junit.jupiter.api.BeforeEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

abstract class BaseControllerTest(userRepository: UserRepository) {

	protected val adminUser: UserData = UserUtils.getUserByEntitlement(Entitlement.ADMINISTRATOR, userRepository)
	protected val editorUser: UserData = UserUtils.getUserByEntitlement(Entitlement.EDITOR, userRepository)
	protected val viewerUser: UserData = UserUtils.getUserByEntitlement(Entitlement.VIEWER, userRepository)
	protected val otherUser: UserData = UserUtils.getUserByEntitlement(Entitlement.MANAGER, userRepository)
	protected val admin = adminUser.convert()
	protected  val editor = editorUser.convert()
	protected  val viewer = viewerUser.convert()
	protected  val other = otherUser.convert()
	protected  lateinit var adminToken: String
	protected  lateinit var editorToken: String
	protected  lateinit var viewerToken: String
	protected  lateinit var otherToken: String
	private var tokenGenerated: Boolean = false

	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	@BeforeEach
	fun generateToken() {
		if (tokenGenerated) return
		adminToken = AuthUtils.getAuthToken(client, admin)
		editorToken = AuthUtils.getAuthToken(client, editor)
		viewerToken = AuthUtils.getAuthToken(client, viewer)
		otherToken = AuthUtils.getAuthToken(client, other)
		tokenGenerated = true
	}

}
