package de.sambalmueslie.openevent.server.entitlement

import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.entitlement.db.ItemEntitlementEntryData
import de.sambalmueslie.openevent.server.entitlement.db.ItemEntitlementEntryRepository
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.user.api.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ItemEntitlementCrudService(private val repository: ItemEntitlementEntryRepository) :
	BaseCrudService<ItemEntitlementEntry, ItemEntitlementChangeRequest, ItemEntitlementEntryData>(repository, logger) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(ItemEntitlementCrudService::class.java)
	}

	override fun create(user: User, request: ItemEntitlementChangeRequest): ItemEntitlementEntry {
		val existing = repository.findByUserIdAndItemIdAndType(user.id, request.itemId, request.type)
		if (existing != null) {
			return update(user, existing, request)
		}

		val data = ItemEntitlementEntryData.convert(user, request)
		return repository.save(data).convert()
	}

	override fun update(user: User, objId: Long, request: ItemEntitlementChangeRequest): ItemEntitlementEntry {
		val current = repository.findByIdOrNull(objId) ?: return create(user, request)
		return update(user, current, request)
	}

	override fun update(user: User, obj: ItemEntitlementEntryData, request: ItemEntitlementChangeRequest): ItemEntitlementEntry {
		obj.entitlement = request.entitlement
		return repository.update(obj).convert()
	}

	override fun convert(data: ItemEntitlementEntryData): ItemEntitlementEntry {
		return data.convert()
	}

	fun findByUserIdAndItemIdAndType(userId: Long, itemId: Long, type: ItemType): ItemEntitlementEntry {
		return repository.findByUserIdAndItemIdAndType(userId, itemId, type)?.convert() ?: ItemEntitlementEntry(0L, userId, itemId, type, Entitlement.NONE)
	}

	fun findByUserIdAndItemId(userId: Long, itemId: Long): ItemEntitlementEntry {
		return repository.findByUserIdAndItemId(userId, itemId)
			.maxByOrNull { it.entitlement.level }?.convert()
			?: ItemEntitlementEntry(0L, userId, itemId, ItemType.OTHER, Entitlement.NONE)
	}

}
