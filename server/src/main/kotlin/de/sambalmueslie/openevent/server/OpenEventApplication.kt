package de.sambalmueslie.openevent.server

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info


@OpenAPIDefinition(
		info = Info(
				title = "OpenEvent",
				version = "0.0"
		)
)
class OpenEventApplication {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			Micronaut.build()
					.packages("de.sambalmueslie.openevent.server")
					.mainClass(OpenEventApplication::class.java)
					.start()
		}
	}
}
