package de.sambalmueslie.openevent.server.user

import io.micronaut.http.annotation.Controller

@Controller("/api/user")
class UserController(private val service: UserService) {

}
