package de.sambalmueslie.openevent.server.common

import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

abstract class CrudController<T : BusinessObject, O : BusinessObjectChangeRequest>(
	private val userService: UserService
) : CrudAPI<T, O> {

	protected fun getUser(user: Authentication): User {
		return userService.getUser(user) ?: throw InvalidRequestException("Cannot find user for authentication ${user.name}")
	}

}
