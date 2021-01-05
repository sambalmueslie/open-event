package de.sambalmueslie.openevent.server.item.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface ItemDescriptionRepository : PageableRepository<ItemDescriptionData, Long> {
}
