package de.sambalmueslie.openevent.server.messaging

import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.event.api.Event
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageHeader
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
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
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import javax.inject.Inject

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class MessageControllerTest(userRepository: UserRepository) {
	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	@Inject
	lateinit var service: MessageCrudService

	private val baseUrl = "/api/message"
	private val user1: UserData = UserUtils.getFirstUser(userRepository)
	private val user2: UserData = UserUtils.getSecondUser(userRepository)
	private val subject = "Test subject"
	private val content = "Test content"

	@Test
	fun `01 create, read update and delete`() {
		val u = user1.convert()
		val accessToken = AuthUtils.getAuthToken(client,u)

		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, user2.id, null, null)).bearerAuth(accessToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(u, user2.convert(), subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)


		val getRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(accessToken)
		val getResponse = client.toBlocking().exchange(getRequest, Message::class.java)
		assertEquals(HttpStatus.OK, getResponse.status)
		val getResult = getResponse.body()!!
		assertEquals(objId, getResult.id)
		assertEquals(objId, getResult.id)
		assertEquals(MessageHeader(u, user2.convert(), subject, null, null), getResult.header)
		assertEquals(content, getResult.content)
		assertEquals(MessageStatus.CREATED, getResult.status)
		assertEquals(setOf(MessageStatus.CREATED), getResult.statusHistory.keys)

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken)
		val getAllResponse = client.toBlocking().exchange(getAllRequest, pageType())
		assertEquals(HttpStatus.OK, getAllResponse.status)
		val getAllResult = getAllResponse.body()!!
		assertEquals(1, getAllResult.totalSize)
		val first = getAllResult.content.first() as Message
		assertEquals(objId, first.id)
		assertEquals(subject, first.header.subject)
		assertEquals(content, first.content)

		val changedContent = "changed content"
		val updateRequest = HttpRequest.PUT("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, user2.id, null, null)).bearerAuth(accessToken)
		val updateResponse = client.toBlocking().exchange(updateRequest, Message::class.java)
		assertEquals(HttpStatus.OK, updateResponse.status)
		val updateResult = updateResponse.body()
		assertNotNull(updateResult)
		assertEquals(objId, updateResult!!.id)
		assertEquals(subject, updateResult.header.subject)
		assertEquals(content, updateResult.content)

		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(accessToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		val getAllEmptyResult = client.toBlocking()
			.exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(accessToken), pageType())
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertEquals(emptyList<Event>(), getAllEmptyResult.body()?.content)
	}

	@Test
	fun `02 check getter endpoints`() {
		val token1 = AuthUtils.getAuthToken(client, user1.convert())
		val token2 = AuthUtils.getAuthToken(client, user2.convert())
		val u = user1.convert()

		val sentSubject = "send message"
		val receivedSubject = "received message"


		client.toBlocking().exchange(HttpRequest.POST(baseUrl, MessageChangeRequest(sentSubject, content, user2.id, null, null)).bearerAuth(token1), Message::class.java)
		client.toBlocking().exchange(HttpRequest.POST(baseUrl, MessageChangeRequest(receivedSubject, content, user1.id, null, null)).bearerAuth(token2), Message::class.java)

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(token1)
		val getAllResponse = client.toBlocking().exchange(getAllRequest, pageType())
		assertEquals(2, getAllResponse.body()!!.totalSize)

		val sentMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/sent").bearerAuth(token1), pageType())
		assertEquals(HttpStatus.OK, sentMessagesResponse.status)
		val sentMessagesResult = sentMessagesResponse.body()
		assertNotNull(sentMessagesResult)
		val firstSent = sentMessagesResult!!.content.first() as Message
		assertEquals(sentSubject, firstSent.header.subject)

		val receivedMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/received").bearerAuth(token1), pageType())
		assertEquals(HttpStatus.OK, receivedMessagesResponse.status)
		val receivedMessagesResult = receivedMessagesResponse.body()
		assertNotNull(receivedMessagesResult)
		val firstReceived = receivedMessagesResult!!.content.first() as Message
		assertEquals(receivedSubject, firstReceived.header.subject)

		val unreadMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/unread").bearerAuth(token1), pageType())
		assertEquals(HttpStatus.OK, unreadMessagesResponse.status)
		val unreadMessagesResult = unreadMessagesResponse.body()
		assertNotNull(unreadMessagesResult)
		val firstUnread = unreadMessagesResult!!.content.first() as Message
		assertEquals(receivedSubject, firstUnread.header.subject)

		val unreadMessagesCountResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/unread/count").bearerAuth(token1), Argument.INT)
		assertEquals(HttpStatus.OK, unreadMessagesResponse.status)
		assertEquals(1, unreadMessagesCountResponse.body())
	}

	private fun pageType() = Argument.of(Page::class.java, Message::class.java)
}