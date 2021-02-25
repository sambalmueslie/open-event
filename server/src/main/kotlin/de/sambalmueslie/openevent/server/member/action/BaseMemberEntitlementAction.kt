package de.sambalmueslie.openevent.server.member.action


import de.sambalmueslie.openevent.server.common.Action
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.entitlement.ItemEntitlementCrudService
import de.sambalmueslie.openevent.server.entitlement.api.ItemEntitlementChangeRequest
import de.sambalmueslie.openevent.server.item.api.ItemType
import de.sambalmueslie.openevent.server.member.api.Member
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseMemberEntitlementAction(
        private val entitlementService: ItemEntitlementCrudService,
        private val type: ItemType
) : Action<Member> {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BaseMemberEntitlementAction::class.java)
    }

    override fun handleCommonEvent(event: CommonChangeEvent<Member>) {
        val user = event.user ?: return
        if (!isItemAffected(event.obj)) return
        val request = ItemEntitlementChangeRequest(event.obj.itemId, type, event.obj.entitlement)
        when (event.type) {
            CommonChangeEventType.CREATED -> entitlementService.create(user, request)
            CommonChangeEventType.UPDATED -> entitlementService.update(user, event.obj.itemId, request)
            CommonChangeEventType.DELETED -> entitlementService.delete(user, event.obj.itemId)
        }
    }

    abstract fun isItemAffected(obj: Member): Boolean


}
