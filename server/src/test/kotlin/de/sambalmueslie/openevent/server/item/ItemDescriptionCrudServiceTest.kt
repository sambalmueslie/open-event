package de.sambalmueslie.openevent.server.item

import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
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
	private val title = "Test title"
	private val shortText = "Test short text"
	private val longText = "Test long text"
	private val imageUrl = "Test image url"
	private val iconUrl = "Test icon url"

	@Test
	fun `create update and delete item description`() {
		val u = user.convert()
		val createRequest = ItemDescriptionChangeRequest(title, shortText, longText, imageUrl, iconUrl)
		val createResult = service.create(u, createRequest)
		val objId = createResult.id
		assertEquals(ItemDescription(objId, title, shortText, longText, imageUrl, iconUrl), createResult)

		val changedShortText = "Changed short text"
		val updateRequest = ItemDescriptionChangeRequest(title, changedShortText, longText, imageUrl, iconUrl)
		val updateResult = service.update(u, objId, updateRequest)
		assertEquals(ItemDescription(objId, title, changedShortText, longText, imageUrl, iconUrl), updateResult)

		val getResult = service.get(objId)
		assertNotNull(getResult)
		assertEquals(ItemDescription(objId, title, changedShortText, longText, imageUrl, iconUrl), updateResult)

		val getAllResult = service.getAll(Pageable.from(0))
		assertEquals(1, getAllResult.totalSize)
		assertEquals(listOf(ItemDescription(objId, title, changedShortText, longText, imageUrl, iconUrl)), getAllResult.content)

		service.delete(u, objId)

		assertEquals(null, service.get(objId))
	}

}
