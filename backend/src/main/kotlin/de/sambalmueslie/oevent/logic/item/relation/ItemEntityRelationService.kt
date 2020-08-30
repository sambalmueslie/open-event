package de.sambalmueslie.oevent.logic.item.relation

import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.logic.item.api.Item

interface ItemEntityRelationService<E : BusinessObject> {
	fun add(item: Item, entity: E): List<E>
	fun remove(item: Item, entity: E): List<E>
	fun get(item: Item): List<E>
	fun getByItemId(itemId: Long): List<E>
}
