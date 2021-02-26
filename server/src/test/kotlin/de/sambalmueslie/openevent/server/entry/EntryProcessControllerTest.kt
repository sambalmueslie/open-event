package de.sambalmueslie.openevent.server.entry

import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessChangeRequest
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.db.UserRepository
import de.sambalmueslie.openevent.test.BaseControllerTest
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.time.LocalDateTime

@MicronautTest
@TestMethodOrder(MethodOrderer.MethodName::class)
internal class EntryProcessControllerTest(userRepository: UserRepository) : BaseControllerTest<EntryProcess>(userRepository) {

    private val baseUrl = "/api/entry"
    private val itemId = 1L
    private val type = ItemType.EVENT
    private val entitlement = Entitlement.VIEWER

    @Test
    fun `create, read update and delete - admin`() {
        val createRequest = HttpRequest.POST(baseUrl, EntryProcessChangeRequest(itemId, type, entitlement)).bearerAuth(adminToken)
        val createResult = client.toBlocking().exchange(createRequest, EntryProcess::class.java)
        assertEquals(HttpStatus.OK, createResult.status)
        val createProcess = createResult.body()!!
        assertEquals(EntryProcess(createProcess.id, admin, itemId, type, entitlement, EntryProcessStatus.REQUESTED), createProcess)

        val getRequest = HttpRequest.GET<String>("$baseUrl/${createProcess.id}").bearerAuth(adminToken)
        val getResult = client.toBlocking().exchange(getRequest, EntryProcess::class.java)
        assertEquals(HttpStatus.OK, getResult.status)
        assertEquals(createProcess, getResult.body())

        val getAllRequest = HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken)
        val getAllResult = client.toBlocking().exchange(getAllRequest, Argument.of(Page::class.java, EntryProcess::class.java))
        assertEquals(HttpStatus.OK, getAllResult.status)
        assertEquals(listOf(createProcess), getAllResult.body()?.content)


        val updateRequest = HttpRequest.PUT("$baseUrl/${createProcess.id}", EntryProcessChangeRequest(itemId, type, Entitlement.EDITOR)).bearerAuth(adminToken)
        val updateResult = client.toBlocking().exchange(updateRequest, EntryProcess::class.java)
        assertEquals(HttpStatus.OK, updateResult.status)
        assertEquals(EntryProcess(createProcess.id, admin, itemId, type, Entitlement.EDITOR, EntryProcessStatus.REQUESTED), updateResult.body())

        val deleteRequest = HttpRequest.DELETE<Any>("$baseUrl/${createProcess.id}").bearerAuth(adminToken)
        val deleteResult = client.toBlocking().exchange(deleteRequest, Argument.STRING)
        assertEquals(HttpStatus.OK, deleteResult.status)

        val getAllEmptyResult = client.toBlocking()
            .exchange(HttpRequest.GET<String>(baseUrl).bearerAuth(adminToken), Argument.of(Page::class.java, EntryProcess::class.java))
        assertEquals(HttpStatus.OK, getAllEmptyResult.status)
        assertEquals(emptyList<EntryProcess>(), getAllEmptyResult.body()?.content)

    }


    override fun getDefaultType() = EntryProcess::class.java
}
