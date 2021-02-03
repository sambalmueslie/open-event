package de.sambalmueslie.openevent.server.announcement.api

import de.sambalmueslie.openevent.server.common.CrudAPI
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface AnnouncementAPI : CrudAPI<Announcement, AnnouncementChangeRequest> {
	fun getItemAnnouncements(authentication: Authentication, itemId: Long, pageable: Pageable): Page<Announcement>
}
