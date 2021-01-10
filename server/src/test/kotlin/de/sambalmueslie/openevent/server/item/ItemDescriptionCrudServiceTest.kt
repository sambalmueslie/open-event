package de.sambalmueslie.openevent.server.item

import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest
internal class ItemDescriptionCrudServiceTest(
	userRepo: UserRepository,
	private val service: ItemDescriptionCrudService
) {
	private val user: UserData = UserUtils.getUser(userRepo)

	@Test
	fun `create update and delete item description`() {
		val u = user.convert()
		val createRequest = ItemDescriptionUtil.getCreateRequest()
		val createResult = service.create(u, createRequest)
		val objId = createResult.id
		assertEquals(ItemDescriptionUtil.getCreateDescription(objId), createResult)

		val updateRequest = ItemDescriptionUtil.getUpdateRequest()
		val updateResult = service.update(u, objId, updateRequest)
		assertEquals(ItemDescriptionUtil.getUpdateDescription(objId), updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(ItemDescriptionUtil.getUpdateDescription(objId), updateResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(ItemDescriptionUtil.getUpdateDescription(objId)), getAllResult.content)

		service.delete(u, objId)

		assertEquals(null, service.get(objId))
	}

}
