package de.sambalmueslie.oevent.logic.log


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class LogService {
	val logger: Logger = LoggerFactory.getLogger(LogService::class.java)

}
