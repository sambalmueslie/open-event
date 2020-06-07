package de.sambalmueslie.oevent.logic.location.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface AddressRepository : CrudRepository<AddressData, Long>
