package de.sambalmueslie.openevent.server.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("idp")
class IdpConfig {
	@NotBlank
	var clientId: String = ""
	@NotBlank
	var secret: String = ""
	@NotBlank
	var tokenUrl: String = ""
	@NotBlank
	var baseUrl: String = ""
	@NotBlank
	var realm: String = ""
}
