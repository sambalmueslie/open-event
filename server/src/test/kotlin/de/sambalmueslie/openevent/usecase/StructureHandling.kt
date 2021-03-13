package de.sambalmueslie.openevent.usecase


import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.item.ItemDescriptionUtil
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class StructureHandling(userRepository: UserRepository) : BaseControllerTest<Structure>(userRepository) {

    override fun getDefaultType() = Structure::class.java

    private val structureServiceUrl = "/api/structure"
    private val memberServiceUrl = "/api/member"
    private val entryServiceUrl = "/api/entry"

    @Test
    fun `01 structure handling`() {
        checkEmptyEnvironment()
        val root = createRootStructure()
        val child = createChildStructure(root)

        managerJoinStructure(root)
        viewerJoinStructure(root)

        cleanupData()
    }

    private fun cleanupData() {
        var response = exchange(delete(entryServiceUrl, editorToken), Argument.of(EntryProcess::class.java))
        assertEquals(HttpStatus.OK, response.status)
        response = exchange(delete(entryServiceUrl, viewerToken), Argument.of(EntryProcess::class.java))
        assertEquals(HttpStatus.OK, response.status)
        response = exchange(delete(entryServiceUrl, otherToken), Argument.of(EntryProcess::class.java))
        assertEquals(HttpStatus.OK, response.status)
        response = exchange(delete(entryServiceUrl, adminToken), Argument.of(EntryProcess::class.java))
        assertEquals(HttpStatus.OK, response.status)
    }


    private fun managerJoinStructure(structure: Structure) {
        // do the request
        val request = EntryProcessChangeRequest(structure.id, ItemType.STRUCTURE, Entitlement.MANAGER)
        var response = callPost(entryServiceUrl, request, otherToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, other, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.REQUESTED), response)

        // get value
        assertTrue(pageEquals(setOf(response.id), callGetPage(entryServiceUrl, adminToken, EntryProcess::class.java)))
        assertTrue(pageEquals(setOf(response.id), callGetPage(entryServiceUrl, otherToken, EntryProcess::class.java)))
        assertTrue(pageEquals(emptySet(), callGetPage(entryServiceUrl, editorToken, EntryProcess::class.java)))
        assertTrue(pageEquals(emptySet(), callGetPage(entryServiceUrl, viewerToken, EntryProcess::class.java)))

        // get user entry processes (own)
        assertTrue(pageEquals(emptySet(), callGetPage("$entryServiceUrl/user/${admin.id}", adminToken, EntryProcess::class.java)))
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/user/${other.id}", otherToken, EntryProcess::class.java)))
        assertTrue(pageEquals(emptySet(), callGetPage("$entryServiceUrl/user/${editor.id}", editorToken, EntryProcess::class.java)))
        assertTrue(pageEquals(emptySet(), callGetPage("$entryServiceUrl/user/${viewer.id}", viewerToken, EntryProcess::class.java)))

        // get user entry processes (other)
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/user/${other.id}", adminToken, EntryProcess::class.java)))
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/user/${other.id}", otherToken, EntryProcess::class.java)))
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/user/${other.id}", editorToken, EntryProcess::class.java) }
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/user/${other.id}", viewerToken, EntryProcess::class.java) }

        // get item entry processes
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/item/${structure.id}", adminToken, EntryProcess::class.java)))
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/item/${structure.id}", otherToken, EntryProcess::class.java) }
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/item/${structure.id}", editorToken, EntryProcess::class.java) }
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/item/${structure.id}", viewerToken, EntryProcess::class.java) }

        // accept the request
        val acceptUrl = "$entryServiceUrl/${response.id}/accept"
        assertThrows(HttpClientResponseException::class.java) { callPut(acceptUrl, "", viewerToken, Argument.of(EntryProcess::class.java)) }
        assertThrows(HttpClientResponseException::class.java) { callPut(acceptUrl, "", editorToken, Argument.of(EntryProcess::class.java)) }
        response = callPut(acceptUrl, "", otherToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, other, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.REQUESTED), response)

        response = callPut(acceptUrl, "", adminToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, other, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.ACCEPTED), response)

        val memberUrl = "$memberServiceUrl/item/${structure.id}"
        assertThrows(HttpClientResponseException::class.java) { callGetPage(memberUrl, viewerToken, Member::class.java) }
        assertThrows(HttpClientResponseException::class.java) { callGetPage(memberUrl, editorToken, Member::class.java) }
        var members = callGetPage(memberUrl, otherToken, Member::class.java)
        assertEquals(1, members.totalSize)
        var member = members.firstOrNull()
        assertNotNull(member)
        assertEquals(Member(member!!.id, other, Entitlement.MANAGER, structure.id, false), member)

        members = callGetPage(memberUrl, adminToken, Member::class.java)
        assertEquals(1, members.totalSize)
        member = members.firstOrNull()
        assertNotNull(member)
        assertEquals(Member(member!!.id, other, Entitlement.MANAGER, structure.id, false), member)
    }


    private fun viewerJoinStructure(structure: Structure) {
        // do the request
        val request = EntryProcessChangeRequest(structure.id, ItemType.STRUCTURE, Entitlement.MANAGER)
        var response = callPost(entryServiceUrl, request, viewerToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, viewer, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.REQUESTED), response)

        // get item entry processes
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/item/${structure.id}", adminToken, EntryProcess::class.java)))
        assertTrue(pageEquals(setOf(response.id), callGetPage("$entryServiceUrl/item/${structure.id}", otherToken, EntryProcess::class.java)))
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/item/${structure.id}", editorToken, EntryProcess::class.java) }
        assertThrows(HttpClientResponseException::class.java) { callGetPage("$entryServiceUrl/item/${structure.id}", viewerToken, EntryProcess::class.java) }

        // decline
        response = callPut("$entryServiceUrl/${response.id}/decline", "", viewerToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, viewer, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.REQUESTED), response)
        response = callPut("$entryServiceUrl/${response.id}/decline", "", otherToken, Argument.of(EntryProcess::class.java))
        assertEquals(EntryProcess(response.id, viewer, structure.id, ItemType.STRUCTURE, Entitlement.MANAGER, EntryProcessStatus.DECLINED), response)
    }

    private fun checkEmptyEnvironment() {
        // check empty environment
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", adminToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, adminToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", viewerToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, viewerToken)))
    }

    private fun createRootStructure(): Structure {
        // create structure as a user - fail
        val request = StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, null, true, true)
        assertThrows(HttpClientResponseException::class.java) {
            callPost(structureServiceUrl, request, viewerToken)
        }
        // create structure as admin - succeed
        val root = callPost(structureServiceUrl, request, adminToken)

        // check getter again
        assertTrue(pageEquals(setOf(root.id), callGetPage("${structureServiceUrl}/roots", adminToken)))
        assertTrue(pageEquals(setOf(root.id), callGetPage(structureServiceUrl, adminToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", viewerToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, viewerToken)))
        return root
    }

    private fun createChildStructure(root: Structure): Structure {
        // create child structure as user - fail
        val request = StructureChangeRequest(ItemDescriptionUtil.getCreateRequest(), null, root.id, true, true)
        assertThrows(HttpClientResponseException::class.java) {
            callPost(structureServiceUrl, request, viewerToken)
        }
        // create structure as admin - succeed
        val child = callPost(structureServiceUrl, request, adminToken)

        // check getter again
        assertTrue(pageEquals(setOf(root.id), callGetPage("${structureServiceUrl}/roots", adminToken)))
        assertTrue(pageEquals(setOf(root.id, child.id), callGetPage(structureServiceUrl, adminToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, editorToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, otherToken)))
        assertTrue(pageEquals(emptySet(), callGetPage("${structureServiceUrl}/roots", viewerToken)))
        assertTrue(pageEquals(emptySet(), callGetPage(structureServiceUrl, viewerToken)))
        return child
    }


}
