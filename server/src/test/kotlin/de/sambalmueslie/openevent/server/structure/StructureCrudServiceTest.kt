package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.location.LocationUtil
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class StructureCrudServiceTest(
	userRepo: UserRepository,
	private val service: StructureCrudService
) {
	private val user: UserData = UserUtils.getUser(userRepo)

	@Test
	fun `create new structure without location`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val request = StructureChangeRequest(item, null, null, true)

		val result = service.create(user.convert(), request)
		assertNotNull(result)
		val owner = user.convert()
		val description = ItemDescriptionUtil.getCreateDescription(result!!.description.id)
		assertEquals(Structure(result.id, true, true, true, owner, description, null, emptyList()), result)
	}

	@Test
	fun `create new event with location`() {
		val item = ItemDescriptionUtil.getCreateRequest()
		val locationRequest = LocationUtil.getCreateRequest()
		val request = StructureChangeRequest(item, locationRequest, null, true)

		val result = service.create(user.convert(), request)
		assertNotNull(result)

		val owner = user.convert()
		val description = ItemDescriptionUtil.getCreateDescription(result!!.description.id)
		val location = LocationUtil.getCreateLocation(result.location!!.id)
		assertEquals(Structure(result.id, true, true, true, owner, description, location, emptyList()), result)
	}

	@Test
	fun `create new structure with children`() {
		val parentRequest = StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true)
		val parent = service.create(user.convert(), parentRequest)
		assertNotNull(parent)

		val childRequest = StructureChangeRequest(ItemDescriptionUtil.getUpdateRequest(), null, parent!!.id, true)
		val children = service.create(user.convert(), childRequest)
		assertNotNull(children)

		val parentWithChildren = service.get(parent.id)
		assertNotNull(children)

		assertEquals(listOf(children), parentWithChildren!!.children)

	}
}
