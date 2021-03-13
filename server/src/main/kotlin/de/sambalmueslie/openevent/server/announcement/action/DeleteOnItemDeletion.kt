package de.sambalmueslie.openevent.server.announcement.action


import de.sambalmueslie.openevent.server.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class DeleteOnItemDeletion(
	itemService: ItemDescriptionCrudService,
	private val service: AnnouncementCrudService
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(DeleteOnItemDeletion::class.java)
	}

	init {
		itemService.register(object : BusinessObjectChangeListener<ItemDescription> {
			override fun handleCommonEvent(event: CommonChangeEvent<ItemDescription>) {
				if (event.type == CommonChangeEventType.DELETED) {
					service.deleteAllForItem(event.user!!, event.obj.id)
				}
			}
		})
	}


}
