package de.sambalmueslie.openevent.server.auth

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = [InsufficientPermissionsException::class, ExceptionHandler::class])
class InsufficientPermissionsExceptionHandler : ExceptionHandler<InsufficientPermissionsException, HttpResponse<*>> {
	override fun handle(request: HttpRequest<*>, exception: InsufficientPermissionsException): HttpResponse<Any> {
		return HttpResponse.unauthorized()
	}
}
