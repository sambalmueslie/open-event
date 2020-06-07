package de.sambalmueslie.oevent.logic.common

import io.micronaut.data.repository.CrudRepository

fun <E, ID> CrudRepository<E, ID>.findByIdOrNull(id: ID): E? {
	return this.findById(id).orElseGet { null }
}
