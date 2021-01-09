package de.sambalmueslie.openevent.server.structure.db

import de.sambalmueslie.openevent.server.item.db.ItemRepository
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import io.micronaut.data.repository.PageableRepository

interface StructureRepository : ItemRepository<StructureData> {
}
