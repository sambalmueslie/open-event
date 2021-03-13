package de.sambalmueslie.openevent.server.entry.action


import de.sambalmueslie.openevent.server.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.server.common.CommonChangeEvent
import de.sambalmueslie.openevent.server.entry.EntryProcessCrudService
import de.sambalmueslie.openevent.server.entry.api.EntryProcess
import de.sambalmueslie.openevent.server.entry.api.EntryProcessStatus
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class DeleteOnAccept(private val service: EntryProcessCrudService) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DeleteOnAccept::class.java)
    }

    init {
        service.register(object : BusinessObjectChangeListener<EntryProcess> {
            override fun handleCommonEvent(event: CommonChangeEvent<EntryProcess>) {
                if(event.obj.status == EntryProcessStatus.ACCEPTED){
                    handleAccepted(event)
                }
            }
        })
    }

    private fun handleAccepted(event: CommonChangeEvent<EntryProcess>) {
        service.delete(event.user!!, event.obj.id)
    }
}
