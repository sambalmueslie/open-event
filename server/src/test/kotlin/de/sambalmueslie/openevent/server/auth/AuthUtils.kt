package de.sambalmueslie.openevent.server.auth


import de.sambalmueslie.openevent.server.user.api.User
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

		fun getAuthToken(client: HttpClient, user: User): String {
			val credentials = UsernamePasswordCredentials(user.externalId, "password")
			val request: HttpRequest<Any> = HttpRequest.POST("/login", credentials)
			val rsp: HttpResponse<BearerAccessRefreshToken> = client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java)
			Assertions.assertEquals(HttpStatus.OK, rsp.status)

			val bearerAccessRefreshToken: BearerAccessRefreshToken = rsp.body()!!
			return bearerAccessRefreshToken.accessToken
		}
	}


}
