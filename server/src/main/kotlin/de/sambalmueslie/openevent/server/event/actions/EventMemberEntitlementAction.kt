package de.sambalmueslie.openevent.server.event.actions


import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.event.db.EventRepository
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.member.action.BaseMemberEntitlementAction
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.structure.db.StructureRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventMemberEntitlementAction(
        private val repository: EventRepository,
        entitlementService: ItemEntitlementCrudService
) : BaseMemberEntitlementAction(entitlementService, ItemType.EVENT) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(EventMemberEntitlementAction::class.java)
    }

    override fun isItemAffected(obj: Member) = repository.existsById(obj.itemId)

}
