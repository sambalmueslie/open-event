package de.sambalmueslie.openevent.server.messaging.api

import de.sambalmueslie.openevent.server.common.CrudAPI
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface MessagingAPI : CrudAPI<Message, MessageChangeRequest> {
	fun getReceivedMessages(authentication: Authentication, pageable: Pageable): Page<Message>
	fun getSentMessages(authentication: Authentication, pageable: Pageable): Page<Message>
	fun getUnreadMessages(authentication: Authentication, pageable: Pageable): Page<Message>
	fun getUnreadMessageCount(authentication: Authentication): Int

	fun markRead(authentication: Authentication, messageId: Long): Message?
}
