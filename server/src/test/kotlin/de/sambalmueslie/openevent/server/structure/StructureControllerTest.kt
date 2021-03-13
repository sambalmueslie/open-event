package de.sambalmueslie.openevent.server.structure

import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class StructureControllerTest(userRepository: UserRepository) : BaseControllerTest<Structure>(userRepository) {

    private val baseUrl = "/api/structure"

    @Test
    fun `01 create, read update and delete`() {
    }


    @Test
    fun `02 check getter endpoints`() {
        val root = callPost(baseUrl, StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true, true), adminToken)
        val children = callPost(baseUrl, StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, root.id, true, true), adminToken)

        assertTrue(pageEquals(setOf(root.id), callGetPage("${baseUrl}/roots", adminToken)))
        assertTrue(pageEquals(setOf(children.id), callGetPage("${baseUrl}/${root.id}/children", adminToken)))
        assertTrue(pageEquals(setOf(root.id, children.id), callGetPage(baseUrl, adminToken)))

    }

    @Test
    fun `03 get update and delete as other - not allowed`() {
        val root = callPost(baseUrl, StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true, true), adminToken)
        val children = callPost(baseUrl, StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, root.id, true, true), adminToken)

        // create as other
        assertThrows(HttpClientResponseException::class.java) {
            callPost(baseUrl, StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true, true), otherToken)
        }

        // get as other
        assertThrows(HttpClientResponseException::class.java) {
            callGet("$baseUrl/${root.id}", otherToken)
        }

        // get all as other
        assertTrue(pageEquals(emptySet(), callGetPage(baseUrl, otherToken)))
        // get root as other
        assertTrue(pageEquals(emptySet(), callGetPage("${baseUrl}/roots", otherToken)))
        // get children as other
        assertTrue(pageEquals(emptySet(), callGetPage("${baseUrl}/${root.id}/children", otherToken)))

        // update as other
        assertThrows(HttpClientResponseException::class.java) {
            callPut("$baseUrl/${root.id}", StructureChangeRequest(ItemDescriptionUtil.getUpdateRequest(), null, null, true, true), otherToken)
        }

        // delete as other
        assertThrows(HttpClientResponseException::class.java) {
            doDelete("$baseUrl/${root.id}", otherToken)
        }

        // check still existing
        assertTrue(pageEquals(setOf(root.id, children.id), callGetPage(baseUrl, adminToken)))
    }


    @Test
    fun `04 create, read update and delete - admin`() {
        val createRequest = StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true, true)
        val createStructure = callPost(baseUrl, createRequest, adminToken, Structure::class.java)
        val structure = Structure(createStructure.id, true, true, true, true, admin, ItemDescriptionUtil.getCreateDescription(createStructure.description.id), null)
        assertEquals(structure, createStructure)

        assertEquals(structure, callGet("$baseUrl/${structure.id}", adminToken, Structure::class.java))

        assertTrue(pageEquals(setOf(structure.id), callGetPage(baseUrl, adminToken, Structure::class.java)))

        val updateItem = ItemDescriptionUtil.getUpdateRequest()
        val updateDescription = ItemDescriptionUtil.getUpdateDescription(createStructure.description.id)
        val updateStructure = Structure(createStructure.id, true, true, true, true, admin, updateDescription, null)
        val updateRequest = StructureChangeRequest(updateItem, null, null, true, true)
        assertEquals(updateStructure, callPut("$baseUrl/${structure.id}", updateRequest, adminToken, Structure::class.java))

        assertEquals(HttpStatus.OK, doDelete("$baseUrl/${structure.id}", adminToken).status)

        assertTrue(pageEquals(emptySet(), callGetPage(baseUrl, adminToken, Structure::class.java)))
    }

    @AfterEach
    fun deleteAll() {
        client.toBlocking().exchange(HttpRequest.DELETE<Any>(baseUrl).bearerAuth(adminToken), Argument.STRING)
    }

    override fun getDefaultType() = Structure::class.java
}
