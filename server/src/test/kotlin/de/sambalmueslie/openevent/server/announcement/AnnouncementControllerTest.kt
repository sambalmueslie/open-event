package de.sambalmueslie.openevent.server.announcement

import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.event.api.Event
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class AnnouncementControllerTest(userRepo: UserRepository) {
	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	private val baseUrl = "/api/announcement"
	private val user: UserData = UserUtils.getFirstUser(userRepo)
	private val subject = "Test subject"
	private val content = "Test content"
	private val itemId = 10L

	@Test
	fun `create, read update and delete`() {
		val accessToken = AuthUtils.getAuthToken(client)
		val u = user.convert()

		val createRequest = HttpRequest.POST(baseUrl, AnnouncementChangeRequest(subject, content, itemId)).bearerAuth(accessToken)
		val createResponse = client.toBlocking().exchange(createRequest, Announcement::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id
		assertEquals(objId, createResult.id)
		assertEquals(subject, createResult.subject)
		assertEquals(content, createResult.content)
		assertEquals(itemId, createResult.itemId)
		assertEquals(u, createResult.author)


		val getRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(accessToken)
		val getResponse = client.toBlocking().exchange(getRequest, Announcement::class.java)
		assertEquals(HttpStatus.OK, getResponse.status)
		val getResult = getResponse.body()!!
		assertEquals(objId, getResult.id)
		assertEquals(subject, getResult.subject)
		assertEquals(content, getResult.content)
		assertEquals(itemId, getResult.itemId)
		assertEquals(u, getResult.author)

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken)
		val getAllResponse = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, Announcement::class.java))
		assertEquals(HttpStatus.OK, getAllResponse.status)
		val getAllResult = getAllResponse.body()!!
		assertEquals(1, getAllResult.totalSize)
		val first = getAllResult.content.first() as Announcement
		assertEquals(objId, first.id)
		assertEquals(subject, first.subject)
		assertEquals(content, first.content)

		val changedContent = "changed content"
		val updateRequest = HttpRequest.PUT("$baseUrl/$objId", AnnouncementChangeRequest(subject, changedContent, itemId)).bearerAuth(accessToken)
		val updateResponse = client.toBlocking().exchange(updateRequest, Announcement::class.java)
		assertEquals(HttpStatus.OK, updateResponse.status)
		val updateResult = updateResponse.body()
		assertNotNull(updateResult)
		assertEquals(objId, updateResult!!.id)
		assertEquals(subject, updateResult.subject)
		assertEquals(changedContent, updateResult.content)

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(accessToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken), Argument.of(Page::class.java, Announcement::class.java))
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Event>(), getAllEmptyResult.body()?.content)

	}
}
