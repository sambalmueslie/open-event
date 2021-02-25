package de.sambalmueslie.openevent.server.structure.db

import de.sambalmueslie.openevent.server.item.db.ItemRepository
import de.sambalmueslie.openevent.server.structure.api.StructureChangeRequest
import io.micronaut.data.annotation.Query
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface StructureRepository : ItemRepository<StructureData, StructureChangeRequest> {
    fun findByParentStructureId(parentStructureId: Long, pageable: Pageable): Page<StructureData>
    fun findByParentStructureIdIsNull(pageable: Pageable): Page<StructureData>

    @Query(
            value = """
                SELECT s.*
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE s.parent_structure_id IS NULL
                  AND ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			""",
            countQuery = """
			    SELECT COUNT(*)
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE s.parent_structure_id IS NULL
                  AND ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			"""
    )
    fun getAllRootAccessible(userId: Long, pageable: Pageable): Page<StructureData>

    @Query(
            value = """
                SELECT s.*
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE s.parent_structure_id = :parentId
                  AND ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			""",
            countQuery = """
			    SELECT COUNT(*)
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE s.parent_structure_id = :parentId
                  AND ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			"""
    )
    fun getAllChildAccessible(userId: Long, parentId: Long, pageable: Pageable): Page<StructureData>

    @Query(
            value = """
                SELECT s.*
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			""",
            countQuery = """
			    SELECT COUNT(*)
                FROM structure AS s
                         JOIN item_entitlement_entry AS i ON i.item_id = s.id
                         JOIN member AS m ON m.item_id = s.id
                WHERE ((i.user_id = :userId AND i.entitlement != 'NONE') OR s.public = true OR (m.user_id = :userId))
			"""
    )
    fun getAllAccessible(userId: Long, pageable: Pageable): Page<StructureData>
}
