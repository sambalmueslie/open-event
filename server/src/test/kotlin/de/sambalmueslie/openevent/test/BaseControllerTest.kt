package de.sambalmueslie.openevent.test


import de.sambalmueslie.openevent.server.auth.AuthUtils
import de.sambalmueslie.openevent.server.common.BusinessObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.messaging.api.Message
import de.sambalmueslie.openevent.server.structure.api.Structure
import de.sambalmueslie.openevent.server.user.UserUtils
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import io.micronaut.core.type.Argument
import io.micronaut.data.model.Page
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import org.junit.jupiter.api.BeforeEach
import javax.inject.Inject

abstract class BaseControllerTest<T>(userRepository: UserRepository) {

	protected val adminUser: UserData = UserUtils.getUserByEntitlement(Entitlement.ADMINISTRATOR, userRepository)
	protected val editorUser: UserData = UserUtils.getUserByEntitlement(Entitlement.EDITOR, userRepository)
	protected val viewerUser: UserData = UserUtils.getUserByEntitlement(Entitlement.VIEWER, userRepository)
	protected val otherUser: UserData = UserUtils.getUserByEntitlement(Entitlement.MANAGER, userRepository)
	protected val admin = adminUser.convert()
	protected val editor = editorUser.convert()
	protected val viewer = viewerUser.convert()
	protected val other = otherUser.convert()
	protected lateinit var adminToken: String
	protected lateinit var editorToken: String
	protected lateinit var viewerToken: String
	protected lateinit var otherToken: String
	private var tokenGenerated: Boolean = false

	@Inject
	@field:Client("/")
	lateinit var client: RxHttpClient

	@BeforeEach
	fun generateToken() {
		if (tokenGenerated) return
		adminToken = AuthUtils.getAuthToken(client, admin)
		editorToken = AuthUtils.getAuthToken(client, editor)
		viewerToken = AuthUtils.getAuthToken(client, viewer)
		otherToken = AuthUtils.getAuthToken(client, other)
		tokenGenerated = true
	}

	fun <T> post(uri: String, body: T, token: String) =  HttpRequest.POST(uri, body).bearerAuth(token)
	fun <T> put(uri: String, body: T, token: String) = HttpRequest.PUT(uri, body).bearerAuth(token)
	fun get(uri: String, token: String) = HttpRequest.GET<String>(uri).bearerAuth(token)
	fun delete(uri: String, token: String) = HttpRequest.DELETE<Any>(uri).bearerAuth(token)

	fun <I, O> doPost(uri: String, body: I, token: String, responseType: Argument<O>) = exchange(post(uri, body, token), responseType)
	fun <I, O> doPut(uri: String, body: I, token: String, responseType: Argument<O>) = exchange(put(uri, body, token), responseType)
	fun <O> doGet(uri: String, token: String, responseType: Argument<O>) = exchange(get(uri, token), responseType)
	fun <O> doDelete(uri: String, token: String, responseType: Argument<O>) = exchange(delete(uri, token), responseType)

	fun <I, O> doPost(uri: String, body: I, token: String, responseType: Class<O>) = exchange(post(uri, body, token), responseType)
	fun <I, O> doPut(uri: String, body: I, token: String, responseType: Class<O>) = exchange(put(uri, body, token), responseType)
	fun <O> doGet(uri: String, token: String, responseType: Class<O>) = exchange(get(uri, token), responseType)
	fun <O> doDelete(uri: String, token: String, responseType: Class<O>) = exchange(delete(uri, token), responseType)

	fun <I> doPost(uri: String, body: I, token: String) = exchange(post(uri, body, token), Any::class.java)
	fun <I> doPut(uri: String, body: I, token: String) = exchange(put(uri, body, token), Any::class.java)
	fun doGet(uri: String, token: String) = exchange(get(uri, token), Any::class.java)
	fun doDelete(uri: String, token: String) = exchange(delete(uri, token), Any::class.java)

	fun <I, O> exchange(request: HttpRequest<I>, bodyType: Argument<O>) = client.toBlocking().exchange(request, bodyType)
	fun <I, O> exchange(request: HttpRequest<I>, bodyType: Class<O>) = client.toBlocking().exchange(request, bodyType)


	fun <I, O> callPost(uri: String, body: I, token: String, responseType: Argument<O>) = retrieve(post(uri, body, token), responseType)
	fun <I, O> callPut(uri: String, body: I, token: String, responseType: Argument<O>) = retrieve(put(uri, body, token), responseType)
	fun <O> callGet(uri: String, token: String, responseType: Argument<O>) = retrieve(get(uri, token), responseType)
	fun <O> callDelete(uri: String, token: String, responseType: Argument<O>) = retrieve(delete(uri, token), responseType)

	fun <I, O> callPost(uri: String, body: I, token: String, responseType: Class<O>) = retrieve(post(uri, body, token), responseType)
	fun <I, O> callPut(uri: String, body: I, token: String, responseType: Class<O>) = retrieve(put(uri, body, token), responseType)
	fun <O> callGet(uri: String, token: String, responseType: Class<O>) = retrieve(get(uri, token), responseType)
	fun <O> callDelete(uri: String, token: String, responseType: Class<O>) = retrieve(delete(uri, token), responseType)


	fun <I> callPost(uri: String, body: I, token: String) = retrieve(post(uri, body, token), getDefaultType())
	fun <I> callPut(uri: String, body: I, token: String) = retrieve(put(uri, body, token), getDefaultType())
	fun callGet(uri: String, token: String) = retrieve(get(uri, token), getDefaultType())
	fun callDelete(uri: String, token: String) = retrieve(delete(uri, token), getDefaultType())

	@Suppress("UNCHECKED_CAST")
	fun <O> callGetPage(uri: String, token: String, responseType: Class<O>) = retrieve(get(uri, token), Argument.of(Page::class.java, responseType)) as Page<O>
	@Suppress("UNCHECKED_CAST")
	fun callGetPage(uri: String, token: String) = retrieve(get(uri, token), Argument.of(Page::class.java, getDefaultType())) as Page<T>

	fun <I, O> retrieve(request: HttpRequest<I>, bodyType: Argument<O>) = client.toBlocking().retrieve(request, bodyType)
	fun <I, O> retrieve(request: HttpRequest<I>, bodyType: Class<O>) = client.toBlocking().retrieve(request, bodyType)


	fun <T: BusinessObject> pageEquals(objectIds: Set<Long>, page: Page<T>): Boolean {
		if (objectIds.size.toLong() != page.totalSize) return false
		return objectIds == page.content.map { it.id }.toSet()
	}

	abstract fun getDefaultType(): Class<T>
}
