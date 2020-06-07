package de.sambalmueslie.oevent

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
		info = Info(
				title = "OpenEvent",
				version = "0.0"
		)
)
object Application {

	@JvmStatic
	fun main(args: Array<String>) {
		Micronaut.build()
				.packages("de.sambalmueslie.oevent")
				.mainClass(Application.javaClass)
				.start()
	}
}
