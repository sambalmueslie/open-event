package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.auth.AuthUtils.Companion.getAuthToken
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.event.api.Period
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
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
	private val user: UserData = UserUtils.getUser(userRepo)
	private val title = "Test title"
	private val shortText = "Test short text"
	private val longText = "Test long text"
	private val imageUrl = "Test image url"
	private val iconUrl = "Test icon url"

	@Test
	fun `create, read update and delete`() {
		val accessToken = getAuthToken(client)
		val item = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val createRequest = HttpRequest.POST(baseUrl, StructureChangeRequest(item, null, null)).bearerAuth(accessToken)
		val createResult = client.toBlocking().exchange(createRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, createResult.status)
		val owner = user.convert()
		val description = ItemDescription(1L, title, shortText, longText, imageUrl, iconUrl)
		val structure = Structure(0L, true, true, true, owner, description, null)
		assertEquals(structure, createResult.body())

		val getRequest = HttpRequest.GET<String>("$baseUrl/${structure.id}").bearerAuth(accessToken)
		val getResult = client.toBlocking().exchange(getRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, getResult.status)
		assertEquals(structure, getResult.body())

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken)
		val getAllResult = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, Structure::class.java))
		assertEquals(HttpStatus.OK, getAllResult.status)
		assertEquals(listOf(structure), getAllResult.body()?.content)

		val updateItem = ItemDescriptionChangeRequest("update title", shortText, longText, imageUrl, iconUrl)
		val updateRequest = HttpRequest.PUT("$baseUrl/${structure.id}", StructureChangeRequest(updateItem, null, null)).bearerAuth(accessToken)
		val updateResult = client.toBlocking().exchange(updateRequest, Structure::class.java)
		assertEquals(HttpStatus.OK, updateResult.status)
		val updateDescription = ItemDescription(1L, updateItem.title, shortText, longText, imageUrl, iconUrl)
		val updateStructure = Structure(0L, true, true, true, owner, updateDescription, null)
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
