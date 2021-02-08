package de.sambalmueslie.openevent.server.entry.action


import de.sambalmueslie.openevent.server.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.server.category.CategoryCrudService
import de.sambalmueslie.openevent.server.category.db.CategoryItemRelationRepository
import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.entry.EntryProcessCrudService
import de.sambalmueslie.openevent.server.item.ItemDescriptionCrudService
import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.messaging.MessageCrudService
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class DeleteOnItemDeletion(
	itemService: ItemDescriptionCrudService,
	private val service: EntryProcessCrudService
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(DeleteOnItemDeletion::class.java)
	}

	init {
		itemService.register(object : BusinessObjectChangeListener<ItemDescription> {
			override fun handleCommonEvent(event: CommonChangeEvent<ItemDescription>) {
				if (event.type == CommonChangeEventType.DELETED) {
					service.deleteAllForItem(event.user!!,event.obj.id)
				}
			}
		})
	}


}
