package de.sambalmueslie.openevent.server.messaging

import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageHeader
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import javax.inject.Inject

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class MessageControllerTest(userRepository: UserRepository): BaseControllerTest(userRepository) {

	private val subject = "Test subject"
	private val content = "Test content"
	private val baseUrl = "/api/message"

	@Test
	fun `01 create, read update and delete`() {
		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(editor, viewer, subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)

		// get as author (editor)
		val editorGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(editorToken)
		val editorGetResponse = client.toBlocking().exchange(editorGetRequest, Message::class.java)
		assertEquals(HttpStatus.OK, editorGetResponse.status)
		assertEquals(objId, editorGetResponse.body()!!.id)

		// get as recipient (viewer)
		val viewerGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(viewerToken)
		val viewerGetResponse = client.toBlocking().exchange(viewerGetRequest, Message::class.java)
		assertEquals(HttpStatus.OK, viewerGetResponse.status)
		assertEquals(objId, viewerGetResponse.body()!!.id)

		// get all as author (editor)
		val editorGetAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(editorToken)
		val editorGetAllResponse = client.toBlocking().exchange(editorGetAllRequest, pageType())
		assertEquals(HttpStatus.OK, editorGetAllResponse.status)
		assertTrue(pageEquals(setOf(objId), editorGetAllResponse))

		// get all as recipient (viewer)
		val viewerGetAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(viewerToken)
		val viewerGetAllResponse = client.toBlocking().exchange(viewerGetAllRequest, pageType())
		assertEquals(HttpStatus.OK, viewerGetAllResponse.status)
		assertTrue(pageEquals(setOf(objId), viewerGetAllResponse))

		// update as author (editor)
		val changedContent = "changed content"
		val editorUpdateRequest = HttpRequest.PUT("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id)).bearerAuth(editorToken)
		val editorUpdateResponse = client.toBlocking().exchange(editorUpdateRequest, Message::class.java)
		assertEquals(HttpStatus.OK, editorUpdateResponse.status)
		val editorUpdateResult = editorUpdateResponse.body()
		assertNotNull(editorUpdateResult)
		assertEquals(objId, editorUpdateResult!!.id)
		assertEquals(subject, editorUpdateResult.header.subject)
		assertEquals(content, editorUpdateResult.content)

		// update as recipient (viewer)
		val viewerUpdateRequest = HttpRequest.PUT("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id)).bearerAuth(viewerToken)
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(viewerUpdateRequest, Message::class.java)
		}

		// delete as recipient (viewer)
		val viewerDeleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(viewerToken)
		val viewerDeleteResponse = client.toBlocking().exchange(viewerDeleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, viewerDeleteResponse.status)

		// check still existing
		val getRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(editorToken)
		val getResponse = client.toBlocking().exchange(getRequest, Message::class.java)
		assertEquals(HttpStatus.OK, getResponse.status)
		assertEquals(objId, getResponse.body()!!.id)

		// delete as author (editor)
		val editorDeleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(editorToken)
		val editorDeleteResponse = client.toBlocking().exchange(editorDeleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, editorDeleteResponse.status)

		val getAllEmptyResult = client.toBlocking().exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken), pageType())
		assertEquals(HttpStatus.OK, getAllEmptyResult.status)
		assertTrue(pageEquals(setOf(), getAllEmptyResult))
	}


	@Test
	fun `02 check getter endpoints`() {
		val sentSubject = "send message"
		val receivedSubject = "received message"


		client.toBlocking().exchange(
			HttpRequest.POST(baseUrl, MessageChangeRequest(sentSubject, content, viewerUser.id)).bearerAuth(adminToken),
			Message::class.java
		)
		client.toBlocking().exchange(
			HttpRequest.POST(baseUrl, MessageChangeRequest(receivedSubject, content, adminUser.id)).bearerAuth(viewerToken),
			Message::class.java
		)

		val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken)
		val getAllResponse = client.toBlocking().exchange(getAllRequest, pageType())
		assertEquals(2, getAllResponse.body()!!.totalSize)

		val sentMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/sent").bearerAuth(adminToken), pageType())
		assertEquals(HttpStatus.OK, sentMessagesResponse.status)
		val sentMessagesResult = sentMessagesResponse.body()
		assertNotNull(sentMessagesResult)
		val firstSent = sentMessagesResult!!.content.first() as Message
		assertEquals(sentSubject, firstSent.header.subject)

		val receivedMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/received").bearerAuth(adminToken), pageType())
		assertEquals(HttpStatus.OK, receivedMessagesResponse.status)
		val receivedMessagesResult = receivedMessagesResponse.body()
		assertNotNull(receivedMessagesResult)
		val firstReceived = receivedMessagesResult!!.content.first() as Message
		assertEquals(receivedSubject, firstReceived.header.subject)

		val unreadMessagesResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/unread").bearerAuth(adminToken), pageType())
		assertEquals(HttpStatus.OK, unreadMessagesResponse.status)
		val unreadMessagesResult = unreadMessagesResponse.body()
		assertNotNull(unreadMessagesResult)
		val firstUnread = unreadMessagesResult!!.content.first() as Message
		assertEquals(receivedSubject, firstUnread.header.subject)

		val unreadMessagesCountResponse = client.toBlocking().exchange(HttpRequest.GET<Any>("${baseUrl}/unread/count").bearerAuth(adminToken), Argument.INT)
		assertEquals(HttpStatus.OK, unreadMessagesResponse.status)
		assertEquals(1, unreadMessagesCountResponse.body())
	}

	@Test
	fun `03 get update and delete as other - not allowed`() {
		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id

		// get as other
		val otherGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(otherToken)
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(otherGetRequest, Message::class.java)
		}

		// get all as other
		val otherGetAllRequest = HttpRequest.GET<String>("$baseUrl").bearerAuth(otherToken)
		val otherGetAllResponse = client.toBlocking().exchange(otherGetAllRequest, pageType())
		assertEquals(HttpStatus.OK, otherGetAllResponse.status)
		assertTrue(pageEquals(emptySet(), otherGetAllResponse))

		// update as other
		val changedContent = "changed content"
		val editorUpdateRequest = HttpRequest.PUT("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id)).bearerAuth(otherToken)
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(editorUpdateRequest, Message::class.java)
		}

		// delete as other
		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(otherToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		// check still existing
		val editorGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(editorToken)
		val editorGetResponse = client.toBlocking().exchange(editorGetRequest, Message::class.java)
		assertEquals(HttpStatus.OK, editorGetResponse.status)
		assertEquals(objId, editorGetResponse.body()!!.id)
	}

	@Test
	fun `04 get update and delete as admin`() {
		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id

		// get as admin
		val adminGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(adminToken)
		val adminGetResponse = client.toBlocking().exchange(adminGetRequest, Message::class.java)
		assertEquals(HttpStatus.OK, adminGetResponse.status)
		assertEquals(objId, adminGetResponse.body()!!.id)

		// get all as admin
		val adminGetAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken)
		val adminGetAllResponse = client.toBlocking().exchange(adminGetAllRequest, pageType())
		assertEquals(HttpStatus.OK, adminGetAllResponse.status)
		assertTrue(pageEquals(setOf(objId), adminGetAllResponse))

		// update as admin
		val changedContent = "changed content"
		val adminUpdateRequest = HttpRequest.PUT("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id)).bearerAuth(adminToken)
		val adminUpdateResponse = client.toBlocking().exchange(adminUpdateRequest, Message::class.java)
		assertEquals(HttpStatus.OK, adminUpdateResponse.status)
		val adminUpdateResult = adminUpdateResponse.body()
		assertNotNull(adminUpdateResult)
		assertEquals(objId, adminUpdateResult!!.id)
		assertEquals(subject, adminUpdateResult.header.subject)
		assertEquals(content, adminUpdateResult.content)

		// delete as admin
		val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/$objId").bearerAuth(adminToken)
		val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
		assertEquals(HttpStatus.OK, deleteResult.status)

		// check not existing anymore
		val editorGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(editorToken)
		assertThrows(HttpClientResponseException::class.java) {
			val editorGetResponse = client.toBlocking().exchange(editorGetRequest, Message::class.java)
		}
	}

	@Test
	fun `05 update not existing message`() {
		val editorUpdateRequest = HttpRequest.PUT("$baseUrl/4711", MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val editorUpdateResponse = client.toBlocking().exchange(editorUpdateRequest, Message::class.java)
		assertEquals(HttpStatus.OK, editorUpdateResponse.status)
		val editorUpdateResult = editorUpdateResponse.body()
		assertNotNull(editorUpdateResult)
		assertEquals(subject, editorUpdateResult!!.header.subject)
		assertEquals(content, editorUpdateResult.content)
	}

	@Test
	fun `06 mark message read`() {
		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id

		// mark read as author (editor)
		val editorReadRequest = HttpRequest.PUT("$baseUrl/$objId/read", "").bearerAuth(editorToken)
		val editorReadResponse = client.toBlocking().exchange(editorReadRequest, Message::class.java)
		assertEquals(objId, editorReadResponse.body()!!.id)
		assertEquals(MessageStatus.CREATED, editorReadResponse.body()!!.status)

		// mark read as recipient (viewer)
		val viewerReadRequest = HttpRequest.PUT("$baseUrl/$objId/read", "").bearerAuth(viewerToken)
		val viewerReadResponse = client.toBlocking().exchange(viewerReadRequest, Message::class.java)
		assertEquals(MessageStatus.READ, viewerReadResponse.body()!!.status)
	}

	@Test
	fun `07 reply to message`() {
		val createRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, viewerUser.id)).bearerAuth(editorToken)
		val createResponse = client.toBlocking().exchange(createRequest, Message::class.java)
		assertEquals(HttpStatus.OK, createResponse.status)
		val createResult = createResponse.body()!!
		val objId = createResult.id

		val replyRequest = HttpRequest.POST(baseUrl, MessageChangeRequest(subject, content, editorUser.id, objId)).bearerAuth(viewerToken)
		val replyResponse = client.toBlocking().exchange(replyRequest, Message::class.java)
		assertEquals(HttpStatus.OK, replyResponse.status)
		val replyResult = replyResponse.body()!!
		assertEquals(objId, replyResult.header.parentMessageId)

		// get as author (editor)
		val editorGetRequest = HttpRequest.GET<String>("$baseUrl/$objId").bearerAuth(editorToken)
		val editorGetResponse = client.toBlocking().exchange(editorGetRequest, Message::class.java)
		assertEquals(HttpStatus.OK, editorGetResponse.status)
		assertEquals(objId, editorGetResponse.body()!!.id)
		assertEquals(MessageStatus.REPLIED, editorGetResponse.body()!!.status)
	}

	private fun pageType() = Argument.of(Page::class.java, Message::class.java)

	@AfterEach
	fun deleteAll() {
		client.toBlocking().exchange(HttpRequest.DELETE<Any>(baseUrl).bearerAuth(adminToken), Argument.STRING)
	}

	@Suppress("UNCHECKED_CAST")
	private fun pageEquals(objectIds: Set<Long>, response: HttpResponse<Page<*>>): Boolean {
		val page = response.body() as Page<Message>? ?: return false
		if (objectIds.size.toLong() != page.totalSize) return false
		return objectIds == page.content.map { it.id }.toSet()
	}
}
