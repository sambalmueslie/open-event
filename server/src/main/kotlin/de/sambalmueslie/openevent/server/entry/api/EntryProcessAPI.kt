package de.sambalmueslie.openevent.server.entry.api

import de.sambalmueslie.openevent.server.common.CrudAPI
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface EntryProcessAPI : CrudAPI<EntryProcess, EntryProcessChangeRequest> {
	fun getUserEntryProcesses(authentication: Authentication, userId: Long, pageable: Pageable): Page<EntryProcess>
	fun getItemEntryProcesses(authentication: Authentication, itemId: Long, pageable: Pageable): Page<EntryProcess>
}
