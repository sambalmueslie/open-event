package de.sambalmueslie.openevent.server.messaging

import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageChangeRequest
import de.sambalmueslie.openevent.server.messaging.api.MessageHeader
import de.sambalmueslie.openevent.server.messaging.api.MessageStatus
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class MessageCrudServiceTest(
	userRepository: UserRepository,
	private val service: MessageCrudService
) {
	private val user1: UserData = UserUtils.getFirstUser(userRepository)
	private val user2: UserData = UserUtils.getSecondUser(userRepository)
	private val subject = "Test subject"
	private val content = "Test content"

	@Test
	fun `create update and delete announcement`() {
		val u = user1.convert()

		val createResult = service.create(u, MessageChangeRequest(subject, content, user2.id, null, null))
		assertNotNull(createResult)
		val objId = createResult!!.id
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(u, user2.convert(), subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)

		val updateResult = service.update(u, objId, MessageChangeRequest(subject, content, user2.id, null, null))
		assertNotNull(updateResult)
		assertEquals(objId, updateResult!!.id)
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(u, user2.convert(), subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(objId, getResult!!.id)
		assertEquals(objId, createResult.id)
		assertEquals(MessageHeader(u, user2.convert(), subject, null, null), createResult.header)
		assertEquals(content, createResult.content)
		assertEquals(MessageStatus.CREATED, createResult.status)
		assertEquals(setOf(MessageStatus.CREATED), createResult.statusHistory.keys)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		val first = getAllResult.content.first()
		assertEquals(objId, first!!.id)
		assertEquals(subject, first.header.subject)
		assertEquals(content, first.content)

		service.delete(u, objId)
		assertEquals(null, service.get(objId))
	}
}
