package de.sambalmueslie.oevent.logic.location.db

import de.sambalmueslie.oevent.model.AddressData
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface AddressRepository : CrudRepository<AddressData, Long>
