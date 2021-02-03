package de.sambalmueslie.openevent.server.messaging

import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageHeader
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class MessageControllerTest(userRepository: UserRepository) : BaseControllerTest<Message>(userRepository) {

	private val subject = "Test subject"
	private val content = "Test content"
	private val baseUrl = "/api/message"

	@Test
	fun `01 create, read update and delete`() {
		val createResult = callPost(baseUrl, MessageChangeRequest(subject, content, viewerUser.id), editorToken)
		val objId = createResult.id
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(editor, viewer, subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)

		// get as author (editor)
		assertEquals(objId, callGet("$baseUrl/$objId",editorToken)!!.id)

		// get as recipient (viewer)
		assertEquals(objId, callGet("$baseUrl/$objId",viewerToken)!!.id)

		// get all as author (editor)
		assertTrue(pageEquals(setOf(objId), callGetPage(baseUrl, editorToken)))

		// get all as recipient (viewer)
		assertTrue(pageEquals(setOf(objId), callGetPage(baseUrl, viewerToken)))

		// update as author (editor)
		val changedContent = "changed content"
		val editorUpdateResult = callPut("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id), editorToken)
		assertNotNull(editorUpdateResult)
		assertEquals(objId, editorUpdateResult!!.id)
		assertEquals(subject, editorUpdateResult.header.subject)
		assertEquals(content, editorUpdateResult.content)

		// update as recipient (viewer)
		assertThrows(HttpClientResponseException::class.java) {
			callPut("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id), viewerToken)
		}

		// delete as recipient (viewer)
		assertEquals(HttpStatus.OK, doDelete("$baseUrl/$objId", viewerToken).status)

		// check still existing
		assertEquals(objId, callGet("$baseUrl/$objId",editorToken)!!.id)

		// delete as author (editor)
		assertEquals(HttpStatus.OK, doDelete("$baseUrl/$objId", editorToken).status)

		assertTrue(pageEquals(emptySet(), callGetPage(baseUrl, adminToken)))
	}


	@Test
	fun `02 check getter endpoints`() {
		val sentSubject = "send message"
		val receivedSubject = "received message"

		val msg1 = callPost(baseUrl,MessageChangeRequest(sentSubject, content, viewerUser.id), adminToken )
		val msg2 = callPost(baseUrl,MessageChangeRequest(receivedSubject, content, adminUser.id), viewerToken )

		assertTrue(pageEquals(setOf(msg1.id, msg2.id), callGetPage(baseUrl, adminToken)))

		assertTrue(pageEquals(setOf(msg1.id), callGetPage("${baseUrl}/sent", adminToken)))

		assertTrue(pageEquals(setOf(msg2.id), callGetPage("${baseUrl}/received", adminToken)))

		assertTrue(pageEquals(setOf(msg2.id), callGetPage("${baseUrl}/unread", adminToken)))

		assertEquals(1, callGet("${baseUrl}/unread/count",adminToken, Int::class.java))
	}

	@Test
	fun `03 get update and delete as other - not allowed`() {
		val objId = callPost(baseUrl,MessageChangeRequest(subject, content, viewerUser.id),editorToken ).id

		// get as other
		assertThrows(HttpClientResponseException::class.java) {
			callGet("$baseUrl/$objId", otherToken)
		}

		// get all as other
		assertTrue(pageEquals(emptySet(), callGetPage(baseUrl, otherToken)))

		// update as other
		val changedContent = "changed content"
		assertThrows(HttpClientResponseException::class.java) {
			callPut("$baseUrl/$objId",MessageChangeRequest(subject, changedContent, viewerUser.id),otherToken)
		}

		// delete as other
		assertEquals(HttpStatus.OK, doDelete("$baseUrl/$objId", otherToken).status)

		// check still existing
		assertTrue(pageEquals(setOf(objId), callGetPage(baseUrl, editorToken)))
	}

	@Test
	fun `04 get update and delete as admin`() {
		val objId = callPost(baseUrl,MessageChangeRequest(subject, content, viewerUser.id),editorToken ).id

		// get as admin
		assertEquals(objId, callGet("$baseUrl/$objId", adminToken).id)

		// get all as admin
		assertTrue(pageEquals(setOf(objId), callGetPage(baseUrl, adminToken)))

		// update as admin
		val changedContent = "changed content"
		val adminUpdateResult = callPut("$baseUrl/$objId", MessageChangeRequest(subject, changedContent, viewerUser.id), adminToken)
		assertEquals(objId, adminUpdateResult!!.id)
		assertEquals(subject, adminUpdateResult.header.subject)
		assertEquals(content, adminUpdateResult.content)

		// delete as admin
		assertEquals(HttpStatus.OK, doDelete("$baseUrl/$objId", adminToken).status)

		// check not existing anymore
		assertThrows(HttpClientResponseException::class.java) {
			callGet("$baseUrl/$objId", editorToken)
		}
	}

	@Test
	fun `05 update not existing message`() {
		val result = callPut("$baseUrl/4711",MessageChangeRequest(subject, content, viewerUser.id),editorToken)
		assertEquals(subject, result.header.subject)
		assertEquals(content, result.content)
	}

	@Test
	fun `06 mark message read`() {
		val objId = callPost(baseUrl,MessageChangeRequest(subject, content, viewerUser.id),editorToken ).id

		// mark read as author (editor)
		val editorReadResponse = callPut("$baseUrl/$objId/read", "", editorToken)
		assertEquals(objId, editorReadResponse.id)
		assertEquals(MessageStatus.CREATED, editorReadResponse.status)

		// mark read as recipient (viewer)
		assertEquals(MessageStatus.READ, callPut("$baseUrl/$objId/read", "", viewerToken).status)
	}

	@Test
	fun `07 reply to message`() {
		val objId = callPost(baseUrl,MessageChangeRequest(subject, content, viewerUser.id),editorToken ).id

		assertEquals(objId, callPost(baseUrl, MessageChangeRequest(subject, content, editorUser.id, objId), viewerToken).header.parentMessageId)

		// get as author (editor)
		val message = callGet("$baseUrl/$objId", editorToken)
		assertEquals(objId,message.id)
		assertEquals(MessageStatus.REPLIED, message.status)
	}

	@AfterEach
	fun deleteAll() {
		client.toBlocking().exchange(HttpRequest.DELETE<Any>(baseUrl).bearerAuth(adminToken), Argument.STRING)
	}

	override fun getDefaultType() = Message::class.java

}
