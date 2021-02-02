package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.auth.AuthUtils.Companion.getAuthToken
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class StructureControllerTest(userRepository: UserRepository) {
	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient
	private val baseUrl = "/api/structure"

	private val adminUser: UserData = UserUtils.getUserByEntitlement(Entitlement.ADMINISTRATOR, userRepository)
	private val editorUser: UserData = UserUtils.getUserByEntitlement(Entitlement.EDITOR, userRepository)
	private val viewerUser: UserData = UserUtils.getUserByEntitlement(Entitlement.VIEWER, userRepository)
	private val otherUser: UserData = UserUtils.getUserByEntitlement(Entitlement.MANAGER, userRepository)
	private val admin = adminUser.convert()
	private val editor = editorUser.convert()
	private val viewer = viewerUser.convert()
	private val other = otherUser.convert()
	private lateinit var adminToken: String
	private lateinit var editorToken: String
	private lateinit var viewerToken: String
	private lateinit var otherToken: String
	private var tokenGenerated: Boolean = false

	@BeforeEach
	fun generateToken() {
		if (tokenGenerated) return
		adminToken = AuthUtils.getAuthToken(client, admin)
		editorToken = AuthUtils.getAuthToken(client, editor)
		viewerToken = AuthUtils.getAuthToken(client, viewer)
		otherToken = AuthUtils.getAuthToken(client, other)
		tokenGenerated = true
	}


	@Test
	fun `create, read update and delete - admin`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val createRequest = HttpRequest.POST(baseUrl, StructureChangeRequest(item, null, null, true)).bearerAuth(adminToken)
		val createResult = client.toBlocking().exchange(createRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, createResult.status)
		val createStructure = createResult.body()!!

		val description = ItemDescriptionUtil.getCreateDescription(createStructure.description.id)
		val structure = Structure(createStructure.id, true, true, true, admin, description, null)
		assertEquals(structure, createStructure)

		val getRequest = HttpRequest.GET<String>("$baseUrl/${structure.id}").bearerAuth(adminToken)
		val getResult = client.toBlocking().exchange(getRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, getResult.status)
		assertEquals(structure, getResult.body())

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken)
		val getAllResult = client.toBlocking().exchange(getAllRequest, pageType())
		assertEquals(HttpStatus.OK, getAllResult.status)
		assertEquals(listOf(structure), getAllResult.body()?.content)

		val updateItem = ItemDescriptionUtil.getUpdateRequest()
		val updateRequest = HttpRequest.PUT("$baseUrl/${structure.id}", StructureChangeRequest(updateItem, null, null, true)).bearerAuth(adminToken)
		val updateResult = client.toBlocking().exchange(updateRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, updateResult.status)
		val updateDescription = ItemDescriptionUtil.getUpdateDescription(createStructure.description.id)
		val updateStructure = Structure(createStructure.id, true, true, true, admin, updateDescription, null)
		assertEquals(updateStructure, updateResult.body())

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/${structure.id}").bearerAuth(adminToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken), pageType())
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Structure>(), getAllEmptyResult.body()?.content)
	}

	private fun pageType() = Argument.of(Page::class.java, Structure::class.java)
}
