package de.sambalmueslie.openevent.server.member


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.PageableIterator
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import de.sambalmueslie.openevent.server.member.db.MemberConvertContent
import de.sambalmueslie.openevent.server.member.db.MemberData
import de.sambalmueslie.openevent.server.member.db.MemberRepository
import de.sambalmueslie.openevent.server.user.UserService
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class MemberCrudService(
	private val repository: MemberRepository,
	private val userService: UserService,
) : BaseCrudService<Member, MemberChangeRequest, MemberData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(MemberCrudService::class.java)
	}

	override fun convert(data: MemberData): Member {
		val user = userService.getUser(data.userId)!!
		return data.convert(MemberConvertContent(user))
	}

	override fun create(user: User, request: MemberChangeRequest): Member? {
		val existing = repository.findByUserIdAndItemId(user.id, request.itemId)
		if (existing.size > 1) cleanupDuplicates(existing)
		if (existing.isNotEmpty()) return update(user, existing.first(), request)

		val data = MemberData.convert(user, request)
		val result = repository.save(data).convert(MemberConvertContent(user))
		notifyCreated(user, result)
		return result
	}

	private fun cleanupDuplicates(existing: List<MemberData>) {
		repository.deleteAll(existing.subList(1, existing.size))
	}

	override fun update(user: User, objId: Long, request: MemberChangeRequest): Member? {
		val existing = repository.findByIdOrNull(objId) ?: return create(user, request)
		if (existing.itemId != request.itemId) return create(user, request)
		return update(user, existing, request)
	}

	override fun update(user: User, obj: MemberData, request: MemberChangeRequest): Member? {
		val processUser = userService.getUser(obj.userId) ?: return null
		obj.update(request)
		val result = repository.update(obj).convert(MemberConvertContent(processUser))
		notifyUpdated(processUser, result)
		return result
	}

	@Transactional
	open fun deleteAllForItem(user: User, itemId: Long) {
		val iterator = PageableIterator { repository.findByItemId(itemId, it) }
		iterator.forEach { delete(user, it) }
	}

}
