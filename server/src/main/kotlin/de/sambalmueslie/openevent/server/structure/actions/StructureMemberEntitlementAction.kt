package de.sambalmueslie.openevent.server.structure.actions


import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.member.action.BaseMemberEntitlementAction
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.structure.db.StructureRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class StructureMemberEntitlementAction(
        private val repository: StructureRepository,
        entitlementService: ItemEntitlementCrudService
) : BaseMemberEntitlementAction(entitlementService, ItemType.STRUCTURE) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(StructureMemberEntitlementAction::class.java)
    }

    override fun isItemAffected(obj: Member) = repository.existsById(obj.itemId)

}
