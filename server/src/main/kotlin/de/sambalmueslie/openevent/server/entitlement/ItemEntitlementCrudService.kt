package de.sambalmueslie.openevent.server.entitlement

import de.sambalmueslie.openevent.server.common.BaseCrudService
import de.sambalmueslie.openevent.server.common.findByIdOrNull
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementEntry
import de.sambalmueslie.openevent.server.entitlement.db.ItemEntitlementEntryData
import de.sambalmueslie.openevent.server.entitlement.db.ItemEntitlementEntryRepository
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
			return update(user, request, existing)
		}

		val data = ItemEntitlementEntryData.convert(user, request)
		return repository.save(data).convert()
	}

	override fun update(user: User, objId: Long, request: ItemEntitlementChangeRequest): ItemEntitlementEntry {
		val current = repository.findByIdOrNull(objId) ?: return create(user, request)
		return update(user, request, current)
	}

	private fun update(user: User, request: ItemEntitlementChangeRequest, data: ItemEntitlementEntryData): ItemEntitlementEntry {
		data.entitlement = request.entitlement
		return repository.update(data).convert()
	}


}
