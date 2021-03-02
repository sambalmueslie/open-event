package de.sambalmueslie.openevent.server.member.api

import de.sambalmueslie.openevent.server.common.CrudAPI
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface MemberAPI : CrudAPI<Member, MemberChangeRequest> {
    fun getUserMembers(authentication: Authentication, userId: Long, pageable: Pageable): Page<Member>
    fun getItemMembers(authentication: Authentication, itemId: Long, pageable: Pageable): Page<Member>
}
