package de.sambalmueslie.openevent.server

import io.micronaut.runtime.Micronaut


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
