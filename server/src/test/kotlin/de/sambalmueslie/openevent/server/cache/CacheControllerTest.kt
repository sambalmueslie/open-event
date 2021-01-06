package de.sambalmueslie.openevent.server.cache

import de.sambalmueslie.openevent.server.cache.api.CacheSettings
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertThrows
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class CacheControllerTest {

	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	@Test
	fun `call endpoints unauthorized`() {
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(HttpRequest.GET<String>("/api/cache"), Argument.listOf(CacheSettings::class.java))
		}
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(HttpRequest.GET<String>("/api/name/test"), Argument.listOf(CacheSettings::class.java))
		}
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(HttpRequest.PUT("/api/name/test", ""), Argument.listOf(CacheSettings::class.java))
		}
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(HttpRequest.DELETE("/api/name/test", ""), Argument.listOf(CacheSettings::class.java))
		}
		assertThrows(HttpClientResponseException::class.java) {
			client.toBlocking().exchange(HttpRequest.DELETE("/api/name/test/clear", ""), Argument.listOf(CacheSettings::class.java))
		}
	}

}
