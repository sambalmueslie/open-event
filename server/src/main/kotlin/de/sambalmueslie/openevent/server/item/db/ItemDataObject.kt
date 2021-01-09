package de.sambalmueslie.openevent.server.item.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.item.api.Item

interface ItemDataObject<T : Item, D : ItemConvertContent> : DataObject<T, D> {
	val ownerId: Long
	val descriptionId: Long
}
