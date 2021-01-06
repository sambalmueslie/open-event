package de.sambalmueslie.openevent.server.item


import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest
import de.sambalmueslie.openevent.server.item.db.ItemDescriptionData
import de.sambalmueslie.openevent.server.item.db.ItemDescriptionRepository
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ItemDescriptionCrudService(private val repository: ItemDescriptionRepository) :
	BaseCrudService<ItemDescription, ItemDescriptionChangeRequest, ItemDescriptionData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(ItemDescriptionCrudService::class.java)
	}

	override fun create(user: User, request: ItemDescriptionChangeRequest): ItemDescription {
		return createData(user, request).second
	}

	fun createData(user: User, request: ItemDescriptionChangeRequest): Pair<ItemDescriptionData, ItemDescription> {
		val data = repository.save(ItemDescriptionData.convert(request))
		val result = data.convert()
		notifyCommon(CommonChangeEvent(user, result, CommonChangeEventType.CREATED))
		return Pair(data, result)
	}

	override fun update(user: User, objId: Long, request: ItemDescriptionChangeRequest): ItemDescription? {
		TODO("Not yet implemented")
	}


}
