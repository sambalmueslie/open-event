package de.sambalmueslie.openevent.server.auth


import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import org.junit.jupiter.api.Assertions
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthUtils {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(AuthUtils::class.java)

		fun getAuthToken(client: HttpClient): String {
			val credentials = UsernamePasswordCredentials("sherlock", "password")
			val request: HttpRequest<Any> = HttpRequest.POST("/login", credentials)
			val rsp: HttpResponse<BearerAccessRefreshToken> = client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java) // <5>
			Assertions.assertEquals(HttpStatus.OK, rsp.status)

			val bearerAccessRefreshToken: BearerAccessRefreshToken = rsp.body()!!
			return bearerAccessRefreshToken.accessToken
		}
	}


}
