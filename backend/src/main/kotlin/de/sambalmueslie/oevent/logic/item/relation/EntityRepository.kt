package de.sambalmueslie.oevent.logic.item.relation

import de.sambalmueslie.oevent.common.BusinessObject
import de.sambalmueslie.oevent.common.DataObject

interface EntityRepository<E : BusinessObject, T : DataObject<E>> {
	fun findByItem(itemId: Long): List<T>
}
