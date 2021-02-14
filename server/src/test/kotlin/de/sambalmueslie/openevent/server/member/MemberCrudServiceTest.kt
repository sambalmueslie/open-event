package de.sambalmueslie.openevent.server.member

import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
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
internal class MemberCrudServiceTest(
	userRepo: UserRepository,
	private val service: MemberCrudService
) {
	private val adminData = UserUtils.getUserByEntitlement(Entitlement.ADMINISTRATOR, userRepo)
	private val admin = adminData.convert()
	private val objId = 1L
	private val itemId = 2L
	private val entitlement = Entitlement.VIEWER

	@Test
	fun `create update and delete user entry process`() {
		val createResult = service.create(admin, MemberChangeRequest(itemId, entitlement, false))
		assertEquals(Member(objId, admin, entitlement, itemId, false), createResult)

		val updateResult = service.update(admin, objId, MemberChangeRequest(itemId, Entitlement.EDITOR, true))
		assertEquals(Member(objId, admin, Entitlement.EDITOR, itemId, true), updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(Member(objId, admin, Entitlement.EDITOR, itemId, true), getResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(Member(objId, admin, Entitlement.EDITOR, itemId, true)), getAllResult.content)

		service.delete(admin, objId)

		assertEquals(null, service.get(objId))
	}


	@Test
	fun `create with duplicates`() {
		val first = service.create(admin, MemberChangeRequest(itemId, entitlement, false))
		assertEquals(Member(first!!.id, admin, entitlement, itemId, false), first)

		val second = service.create(admin, MemberChangeRequest(itemId, entitlement, false))
		assertEquals(Member(second!!.id, admin, entitlement, itemId, false), second)
	}


	@Test
	fun `various update scenarios`() {
		val updateNotExisting = service.update(admin, 4711L, MemberChangeRequest(itemId, entitlement, false))
		assertEquals(Member(updateNotExisting!!.id, admin, entitlement, itemId, false), updateNotExisting)

		val updateDifferentItem = service.update(admin, 4711L, MemberChangeRequest(itemId+1, entitlement, false))
		assertEquals(Member(updateDifferentItem!!.id, admin, entitlement, itemId+1, false), updateDifferentItem)
	}
}
