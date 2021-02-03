package de.sambalmueslie.openevent.server.structure.api

import de.sambalmueslie.openevent.server.common.CrudAPI
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface StructureAPI : CrudAPI<Structure, StructureChangeRequest> {
	fun getRoots(authentication: Authentication, pageable: Pageable): Page<Structure>
	fun getChildren(authentication: Authentication, objId: Long, pageable: Pageable): Page<Structure>
}
