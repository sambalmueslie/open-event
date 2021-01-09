package de.sambalmueslie.openevent.server.item.api

import de.sambalmueslie.openevent.server.common.BusinessObjectChangeRequest

interface ItemChangeRequest : BusinessObjectChangeRequest {
	val item: ItemDescriptionChangeRequest
}
