package de.sambalmueslie.openevent.server.member.action


import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.common.CommonChangeEventType
import de.sambalmueslie.openevent.server.entry.EntryProcessCrudService
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import de.sambalmueslie.openevent.server.member.MemberCrudService
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class EntryProcessCompletedAction(
	processService: EntryProcessCrudService,
	private val service: MemberCrudService
) {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EntryProcessCompletedAction::class.java)
	}

	init {
		processService.register(object : BusinessObjectChangeListener<EntryProcess> {
			override fun handleCommonEvent(event: CommonChangeEvent<EntryProcess>) {
				when (event.type) {
					CommonChangeEventType.CREATED -> checkForCompleted(event)
					CommonChangeEventType.UPDATED -> checkForCompleted(event)
					else -> return
				}
			}
		})
	}

	private fun checkForCompleted(event: CommonChangeEvent<EntryProcess>) {
		val entryProcess = event.obj
		if (entryProcess.status != EntryProcessStatus.ACCEPTED) return

		val request = MemberChangeRequest(entryProcess.itemId, entryProcess.entitlement, false)
		service.create(entryProcess.user, request)

	}
}
