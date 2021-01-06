package de.sambalmueslie.openevent.server.config

import io.micronaut.context.annotation.ConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.validation.constraints.NotBlank

@ConfigurationProperties("idp")
class IdpConfig {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(IdpConfig::class.java)
	}

	@NotBlank
	var clientId: String = ""
		set(value) {
			logger.debug("Set client id from '$clientId' to '$value'")
			field = value
		}

	@NotBlank
	var secret: String = ""

	@NotBlank
	var tokenUrl: String = ""
		set(value) {
			logger.debug("Set tokenUrl from '$tokenUrl' to '$value'")
			field = value
		}

	@NotBlank
	var baseUrl: String = ""
		set(value) {
			logger.debug("Set baseUrl from '$baseUrl' to '$value'")
			field = value
		}

	@NotBlank
	var realm: String = ""
		set(value) {
			logger.debug("Set realm from '$realm' to '$value'")
			field = value
		}

	@NotBlank
	var backendResource: String = ""
		set(value) {
			logger.debug("Set backendResource from '$backendResource' to '$value'")
			field = value
		}


	@NotBlank
	var adminRoleName: String = "ADMINISTRATOR"
		set(value) {
			logger.debug("Set adminRoleName from '$adminRoleName' to '$value'")
			field = value
		}
}
