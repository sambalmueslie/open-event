package de.sambalmueslie.oevent.logic.item.relation

import de.sambalmueslie.oevent.common.BusinessObject

data  class ItemEntities<E : BusinessObject>(val entities: List<E>)
