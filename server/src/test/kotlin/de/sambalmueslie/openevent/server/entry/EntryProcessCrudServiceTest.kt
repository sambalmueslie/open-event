package de.sambalmueslie.openevent.server.entry

import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class EntryProcessCrudServiceTest(
	userRepo: UserRepository,
	private val service: EntryProcessCrudService
) {
	private val adminData = UserUtils.getUserByEntitlement(Entitlement.ADMINISTRATOR, userRepo)
	private val admin = adminData.convert()
	private val objId = 1L
	private val itemId = 2L
	private val itemType = ItemType.STRUCTURE
	private val entitlement = Entitlement.VIEWER

	@Test
	fun `create update and delete user entitlement`() {
		val createResult = service.create(admin, EntryProcessChangeRequest(itemId, itemType, entitlement))
		assertEquals(EntryProcess(objId, admin, itemId, itemType, entitlement, EntryProcessStatus.REQUESTED), createResult)

		val updateResult = service.update(admin, objId, EntryProcessChangeRequest(itemId, itemType, Entitlement.EDITOR))
		assertEquals(EntryProcess(objId, admin, itemId, itemType, Entitlement.EDITOR, EntryProcessStatus.REQUESTED), updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(EntryProcess(objId, admin, itemId, itemType, Entitlement.EDITOR, EntryProcessStatus.REQUESTED), getResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(EntryProcess(objId, admin, itemId, itemType, Entitlement.EDITOR, EntryProcessStatus.REQUESTED)), getAllResult.content)

		service.delete(admin, objId)

		assertEquals(null, service.get(objId))
	}


	@Test
	fun `various update scenarios`() {
		val updateCreateResult = service.update(admin,4711L, EntryProcessChangeRequest(itemId, itemType, entitlement))
		assertEquals(EntryProcess(updateCreateResult!!.id, admin, itemId, itemType, entitlement, EntryProcessStatus.REQUESTED), updateCreateResult)

		val updateDifferentItemResult = service.update(admin, 4711L, EntryProcessChangeRequest(itemId+1, ItemType.EVENT, entitlement))
		assertEquals(EntryProcess(updateDifferentItemResult!!.id, admin, itemId+1, ItemType.EVENT, entitlement, EntryProcessStatus.REQUESTED), updateDifferentItemResult)
	}
}
