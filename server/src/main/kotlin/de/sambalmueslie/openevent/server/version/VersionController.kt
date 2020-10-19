package de.sambalmueslie.openevent.server.version


import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller("/api/version")
class VersionController {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(VersionController::class.java)
	}

	@Get() fun getVersion() = VersionProperties()


}
