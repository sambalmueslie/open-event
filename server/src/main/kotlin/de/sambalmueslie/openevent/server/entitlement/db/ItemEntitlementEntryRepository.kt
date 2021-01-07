package de.sambalmueslie.openevent.server.entitlement.db

import de.sambalmueslie.openevent.server.item.api.ItemType
import io.micronaut.data.annotation.Join
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import java.util.*

@JdbcRepository(dialect = Dialect.POSTGRES)

interface ItemEntitlementEntryRepository : PageableRepository<ItemEntitlementEntryData, Long> {

	fun findByUserId(userId: Long): List<ItemEntitlementEntryData>

	fun findByUserId(userId: Long, pageable: Pageable): Page<ItemEntitlementEntryData>

	fun findByUserIdAndType(userId: Long, type: ItemType): List<ItemEntitlementEntryData>

	fun findByUserIdAndType(userId: Long, type: ItemType, pageable: Pageable): Page<ItemEntitlementEntryData>

	fun findByUserIdAndItemId(userId: Long, itemId: Long): List<ItemEntitlementEntryData>

	fun findByUserIdAndItemIdAndType(userId: Long, itemId: Long, type: ItemType): ItemEntitlementEntryData?

	fun deleteAllByItemId(itemId: Long)
}
