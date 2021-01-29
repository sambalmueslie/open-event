package de.sambalmueslie.openevent.server.structure.db

import de.sambalmueslie.openevent.server.item.db.ItemRepository
import io.micronaut.data.annotation.Query
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface StructureRepository : ItemRepository<StructureData> {
	fun findByParentStructureId(parentStructureId: Long, pageable: Pageable): Page<StructureData>
	fun findByParentStructureIdIsNull(pageable: Pageable): Page<StructureData>

	@Query(
		value = """
			SELECT s.*
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId
  				AND s.parentStructureId = null
  				AND i.entitlement != 'NONE'
			""",
		countQuery = """
			SELECT COUNT(*)
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId
  				AND s.parentStructureId = null
  				AND i.entitlement != 'NONE'
			"""
	)
	fun getAllRootAccessible(userId: Long, pageable: Pageable): Page<StructureData>

	@Query(
		value = """
			SELECT s.*
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId
  				AND s.parentStructureId = :parentId
  				AND i.entitlement != 'NONE'
			""",
		countQuery = """
			SELECT COUNT(*)
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId
  				AND s.parentStructureId = :parentId
  				AND i.entitlement != 'NONE'
			"""
	)
	fun getAllChildAccessible(userId: Long, parentId: Long, pageable: Pageable): Page<StructureData>

	@Query(
		value = """
			SELECT s.*
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId AND i.entitlement != 'NONE'
			""",
		countQuery = """
			SELECT COUNT(*)
			FROM Structure s
         	JOIN ItemEntitlementEntry i ON i.itemId = s.id
			WHERE i.userId = :userId AND i.entitlement != 'NONE'
			"""
	)
	fun getAllAccessible(userId: Long, pageable: Pageable): Page<StructureData>
}
