package de.sambalmueslie.oevent.user


import de.sambalmueslie.oevent.common.BaseService
import de.sambalmueslie.oevent.user.api.User
import de.sambalmueslie.oevent.user.api.UserChangeRequest
import de.sambalmueslie.oevent.user.db.UserData
import de.sambalmueslie.oevent.user.db.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class UserService(private val repository: UserRepository) : BaseService<User, UserChangeRequest, UserData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
	}

	private val validator = UserValidator(repository)
	override fun getValidator() = validator

	private val merger = UserMerger()
	override fun getMerger() = merger


}
