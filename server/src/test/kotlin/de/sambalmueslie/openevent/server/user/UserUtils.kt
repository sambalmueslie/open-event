package de.sambalmueslie.openevent.server.user


import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class UserUtils {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserUtils::class.java)

		private val userId = "Test user 1"
		private val userName = "Test user name 1"
		private val secondUserId = "Test user 2"
		private val secondUserName = "Test user name 2"

		fun getFirstUser(userRepo: UserRepository): UserData {
			return userRepo.findByUserName(userName).firstOrNull() ?: userRepo.save(createFirstSampleUser())
		}

		fun getSecondUser(userRepo: UserRepository): UserData {
			return userRepo.findByUserName(secondUserName).firstOrNull() ?: userRepo.save(createSecondSampleUser())
		}

		fun createFirstSampleUser() = UserData(0L, userId, userName, "", "", "", "", LocalDateTime.now(), false)
		fun createSecondSampleUser() = UserData(0L, secondUserId, secondUserName, "", "", "", "", LocalDateTime.now(), false)

	}


}
