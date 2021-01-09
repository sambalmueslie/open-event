package de.sambalmueslie.openevent.server.config


import io.micronaut.context.annotation.ConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ConfigurationProperties("event")
class EventConfig {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventConfig::class.java)
	}

	var autoPublish: Boolean = true
		set(value) {
			logger.debug("Set autoPublish from '$autoPublish' to '$value'")
			field = value
		}
}
