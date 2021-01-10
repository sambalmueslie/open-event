package de.sambalmueslie.openevent.server.item

import de.sambalmueslie.openevent.server.item.api.ItemDescription
import de.sambalmueslie.openevent.server.item.api.ItemDescriptionChangeRequest


class ItemDescriptionUtil {
	companion object {
		private val createTitle = "Test title"
		private val updateTitle = "Test title - update"
		private val shortText = "Test short text"
		private val longText = "Test long text"
		private val imageUrl = "Test image url"
		private val iconUrl = "Test icon url"

		fun getCreateRequest() = ItemDescriptionChangeRequest(createTitle, shortText, longText, imageUrl, iconUrl)
		fun getUpdateRequest() = ItemDescriptionChangeRequest(updateTitle, shortText, longText, imageUrl, iconUrl)
		fun getCreateDescription(objId: Long) = ItemDescription(objId, createTitle, shortText, longText, imageUrl, iconUrl)
		fun getUpdateDescription(objId: Long) = ItemDescription(objId, updateTitle, shortText, longText, imageUrl, iconUrl)
	}
}


