package de.sambalmueslie.openevent.server.user


import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class UserUtils {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserUtils::class.java)

		private val userId = "Test user id"
		private val userName = "Test user name"

		fun getUser(userRepo: UserRepository): UserData {
			return userRepo.findByUserName(userName).firstOrNull()
				?: userRepo.save(UserData(0L, userId, userName, "", "", "", "", LocalDateTime.now(), false))
		}

	}


}
