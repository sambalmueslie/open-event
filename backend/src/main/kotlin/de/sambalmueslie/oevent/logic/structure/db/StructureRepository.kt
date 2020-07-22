package de.sambalmueslie.oevent.logic.structure.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository

@Repository
interface StructureRepository : PageableRepository<StructureEntity, Long> {
	fun findByRoot(root: Boolean): List<StructureEntity>
	fun findByRoot(root: Boolean, pageable: Pageable): Page<StructureEntity>

	fun findByRootAndIdIn(root: Boolean, ids: Set<Long>): List<StructureEntity>
	fun findByRootAndIdIn(root: Boolean, ids: Set<Long>, pageable: Pageable): Page<StructureEntity>

//	fun findByParentContains(parent: StructureData): List<StructureData>
//	fun findByParentContains(parent: StructureData, pageable: Pageable): Page<StructureData>
}
