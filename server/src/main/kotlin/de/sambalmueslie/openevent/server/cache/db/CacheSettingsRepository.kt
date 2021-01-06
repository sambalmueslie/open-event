package de.sambalmueslie.openevent.server.cache.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository


@JdbcRepository(dialect = Dialect.POSTGRES)
interface CacheSettingsRepository : CrudRepository<CacheSettingsData, Long> {
    fun findByName(name: String): CacheSettingsData?
    fun findByEnabledTrue(): List<CacheSettingsData>
    fun findByNameIn(names: Set<String>): List<CacheSettingsData>
}
