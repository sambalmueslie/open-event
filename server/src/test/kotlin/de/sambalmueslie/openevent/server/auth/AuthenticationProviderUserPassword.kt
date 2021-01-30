package de.sambalmueslie.openevent.server.auth

import com.nimbusds.jose.shaded.json.JSONObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Singleton

@Singleton
class AuthenticationProviderUserPassword : AuthenticationProvider {

	override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
		return Flowable.create({ emitter: FlowableEmitter<AuthenticationResponse> ->
								   if (authenticationRequest.secret == "password") {
									   val details = UserDetails(
										   authenticationRequest.identity as String,
										   listOf(Entitlement.ADMINISTRATOR.name),
										   mapOf(
											   Pair(
												   "resource_access",
												   JSONObject(
													   mapOf(
														   Pair(
															   "test resource",
															   JSONObject(mapOf(Pair("roles", ArrayList(listOf("ADMINISTRATOR")))))
														   )
													   )
												   )

											   )
										   )
									   )
									   emitter.onNext(details)
									   emitter.onComplete()
								   } else {
									   emitter.onError(AuthenticationException(AuthenticationFailed()))
								   }
							   }, BackpressureStrategy.ERROR)
	}
}
