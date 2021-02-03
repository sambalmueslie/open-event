package de.sambalmueslie.openevent.server.location

import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class LocationCrudServiceTest(
	userRepo: UserRepository,
	private val service: LocationCrudService
) {
	private val user: UserData = UserUtils.getFirstUser(userRepo)

	@Test
	fun `create new location`() {
		val request = LocationUtil.getCreateRequest()
		val result = service.create(user.convert(), request)
		assertEquals(LocationUtil.getCreateLocation(result.id), result)
	}


	@Test
	fun `update location - create, modify, delete`() {
		val request = LocationUtil.getCreateRequest()
		val result = service.update(user.convert(), null, request)
		assertNotNull(result)
		val objId = result!!.id
		assertEquals(LocationUtil.getCreateLocation(objId), result)

		val updateRequest = LocationUtil.getUpdateRequest()
		val updateResult = service.update(user.convert(), objId, updateRequest)
		assertNotNull(updateResult)

		assertEquals(LocationUtil.getUpdateLocation(objId), updateResult)

		val deleteResult = service.update(user.convert(), objId, null)
		assertNull(deleteResult)

		val ignoreResult = service.update(user.convert(), null, null)
		assertNull(ignoreResult)
	}


}
