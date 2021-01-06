package de.sambalmueslie.openevent.server.location.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface AddressRepository : PageableRepository<AddressData, Long> {
}
