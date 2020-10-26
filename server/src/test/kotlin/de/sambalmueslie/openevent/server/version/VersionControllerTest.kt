package de.sambalmueslie.openevent.server.version

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.RxStreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class VersionControllerTest {

	@Inject
	@field:Client("/")
	lateinit var client: RxStreamingHttpClient

	@Test
	fun `get version`() {
		val get = HttpRequest.GET<String>("/api/version")
		val response  = client.toBlocking().exchange(get, VersionProperties::class.java)
		assertEquals(HttpStatus.OK, response.status)
		assertEquals(VersionProperties(), response.body())
	}
}
