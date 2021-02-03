package de.sambalmueslie.openevent.server.announcement

import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class AnnouncementCrudServiceTest(
	userRepo: UserRepository,
	private val service: AnnouncementCrudService
) {

	private val user: UserData = UserUtils.getFirstUser(userRepo)
	private val subject = "Test subject"
	private val content = "Test content"
	private val itemId = 10L

	@Test
	fun `create update and delete announcement`() {
		val u = user.convert()

		val createResult = service.create(u, AnnouncementChangeRequest(subject, content, itemId))
		val objId = createResult.id
		assertEquals(objId, createResult.id)
		assertEquals(subject, createResult.subject)
		assertEquals(content, createResult.content)
		assertEquals(itemId, createResult.itemId)
		assertEquals(u, createResult.author)

		val changedContent = "changed content"
		val updateResult = service.update(u, objId, AnnouncementChangeRequest(subject, changedContent, itemId))
		assertNotNull(updateResult)
		assertEquals(objId, updateResult!!.id)
		assertEquals(subject, updateResult.subject)
		assertEquals(changedContent, updateResult.content)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(objId, getResult!!.id)
		assertEquals(subject, getResult.subject)
		assertEquals(changedContent, getResult.content)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		val first = getAllResult.content.first()
		assertEquals(objId, first!!.id)
		assertEquals(subject, first.subject)
		assertEquals(changedContent, first.content)

		service.delete(u, objId)
		assertEquals(null, service.get(objId))
	}
}
