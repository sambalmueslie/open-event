package de.sambalmueslie.openevent.server.entitlement

import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
internal class ItemEntitlementCrudServiceTest(
	userRepo: UserRepository,
	private val service: ItemEntitlementCrudService
) {

	private val user: UserData = UserUtils.getFirstUser(userRepo)

	@Test
	fun `create update and delete user entitlement`() {
		val objId = 1L
		val itemId = 2L
		val type = ItemType.EVENT
		val entitlement = Entitlement.MANAGER
		val u = user.convert()
		val createResult = service.create(u, ItemEntitlementChangeRequest(itemId, type, entitlement))
		assertEquals(ItemEntitlementEntry(objId, u.id, itemId, type, entitlement), createResult)

		val updateResult = service.update(u, objId, ItemEntitlementChangeRequest(itemId, type, Entitlement.EDITOR))
		assertEquals(ItemEntitlementEntry(objId, u.id, itemId, type, Entitlement.EDITOR), updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(ItemEntitlementEntry(objId, u.id, itemId, type, Entitlement.EDITOR), getResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(ItemEntitlementEntry(objId, u.id, itemId, type, Entitlement.EDITOR)), getAllResult.content)

		service.delete(u, objId)

		assertEquals(null, service.get(objId))
	}

}
