package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.auth.AuthUtils.Companion.getAuthToken
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
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class StructureControllerTest(userRepo: UserRepository) {
	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient
	private val baseUrl = "/api/structure"
	private val user: UserData = UserUtils.getFirstUser(userRepo)


	@Test
	fun `create, read update and delete`() {
		val accessToken = getAuthToken(client, user.convert())
		val item = ItemDescriptionUtil.getCreateRequest()
		val createRequest = HttpRequest.POST(baseUrl, StructureChangeRequest(item, null, null, true)).bearerAuth(accessToken)
		val createResult = client.toBlocking().exchange(createRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, createResult.status)
		val createStructure = createResult.body()!!

		val owner = user.convert()
		val description = ItemDescriptionUtil.getCreateDescription(createStructure.description.id)
		val structure = Structure(createStructure.id, true, true, true, owner, description, null, emptyList())
		assertEquals(structure, createStructure)

		val getRequest = HttpRequest.GET<String>("$baseUrl/${structure.id}").bearerAuth(accessToken)
		val getResult = client.toBlocking().exchange(getRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, getResult.status)
		assertEquals(structure, getResult.body())

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken)
		val getAllResult = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, Structure::class.java))
		assertEquals(HttpStatus.OK, getAllResult.status)
		assertEquals(listOf(structure), getAllResult.body()?.content)

		val updateItem = ItemDescriptionUtil.getUpdateRequest()
		val updateRequest = HttpRequest.PUT("$baseUrl/${structure.id}", StructureChangeRequest(updateItem, null, null, true)).bearerAuth(accessToken)
		val updateResult = client.toBlocking().exchange(updateRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, updateResult.status)
		val updateDescription = ItemDescriptionUtil.getUpdateDescription(createStructure.description.id)
		val updateStructure = Structure(createStructure.id, true, true, true, owner, updateDescription, null, emptyList())
		assertEquals(updateStructure, updateResult.body())

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/${structure.id}").bearerAuth(accessToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken), Argument.of(Page::class.java, Structure::class.java))
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Structure>(), getAllEmptyResult.body()?.content)
	}
}
