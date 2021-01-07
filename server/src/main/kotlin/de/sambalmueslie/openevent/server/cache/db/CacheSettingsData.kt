package de.sambalmueslie.openevent.server.cache.db

import de.sambalmueslie.openevent.server.cache.api.CacheSettings
import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.common.EmptyConvertContent
import javax.persistence.*

@Entity(name = "CacheSettings")
@Table(name = "cache_settings")
data class CacheSettingsData(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,
	@Column(nullable = false, unique = true)
	val name: String = "",
	@Column(nullable = false)
	var enabled: Boolean = false
) : DataObject<CacheSettings, EmptyConvertContent> {

	fun convert() = convert(EmptyConvertContent())
	override fun convert(content: EmptyConvertContent) = CacheSettings(id, name, enabled)
}
