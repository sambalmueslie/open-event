package de.sambalmueslie.openevent.server.auth

import com.nimbusds.jose.shaded.json.JSONObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.user.UserUtils
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
									   emitter.onNext(createUserDetails(authenticationRequest))
									   emitter.onComplete()
								   } else {
									   emitter.onError(AuthenticationException(AuthenticationFailed()))
								   }
							   }, BackpressureStrategy.ERROR)
	}

	private fun createUserDetails(request: AuthenticationRequest<*, *>) = UserDetails(
		request.identity as String,
		listOf(Entitlement.ADMINISTRATOR.name),
		mapOf(Pair("resource_access", JSONObject(mapOf(Pair("test resource", JSONObject(mapOf(Pair("roles", getRole(request)))))))))
	)

	private fun getRole(request: AuthenticationRequest<*, *>): ArrayList<String> {
		return when(request.identity as String){
			UserUtils.adminUserId -> ArrayList(listOf(Entitlement.ADMINISTRATOR.name))
			UserUtils.managerUserId -> ArrayList(listOf(Entitlement.EDITOR.name))
			UserUtils.editorUserId -> ArrayList(listOf(Entitlement.EDITOR.name))
			UserUtils.viewerUserId -> ArrayList(listOf(Entitlement.VIEWER.name))
			else -> ArrayList()
		}
	}
}
