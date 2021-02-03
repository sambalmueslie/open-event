package de.sambalmueslie.openevent.server.user


import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.user.db.UserData
import de.sambalmueslie.openevent.server.user.db.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class UserUtils {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserUtils::class.java)

		private const val userId = "Test user 1"
		private const val userName = "Test user name 1"
		private const val secondUserId = "Test user 2"
		private const val secondUserName = "Test user name 2"

		const val adminUserId = "AdminUserId"
		private const val adminUserName = "AdminUserName"
		const val viewerUserId = "ViewerUserId"
		private const val viewerUserName = "viewerUserName"
		const val managerUserId = "ManagerUserId"
		private const val managerUserName = "managerUserName"
		const val editorUserId = "EditorUserId"
		private const val editorUserName = "editorUserName"

		fun getUserByEntitlement(entitlement: Entitlement, userRepo: UserRepository): UserData {
			return when (entitlement) {
				Entitlement.ADMINISTRATOR -> getUser(adminUserId, adminUserName, userRepo)
				Entitlement.MANAGER -> getUser(managerUserId, managerUserName, userRepo)
				Entitlement.EDITOR -> getUser(editorUserId, editorUserName, userRepo)
				else -> getUser(viewerUserId, viewerUserName, userRepo)
			}
		}

		private fun getUser(userId: String, userName: String, userRepo: UserRepository): UserData {
			return userRepo.findByUserName(userName).firstOrNull() ?: userRepo.save(
				UserData(0L, userId, userName, "", "", "", "", LocalDateTime.now(), false)
			)
		}

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
