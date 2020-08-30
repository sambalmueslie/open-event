package de.sambalmueslie.oevent.logic.item.relation

import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.logic.item.api.Item

interface ItemEntityRelationFactory<T : ItemEntityRelation, E : BusinessObject> {
	fun create(id: String, item: Item, entity: E): T
}
