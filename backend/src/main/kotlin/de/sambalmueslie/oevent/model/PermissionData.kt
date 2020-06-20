package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Permission
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import javax.persistence.*

@Entity(name = "Permission")
@Table(name = "permission")
data class PermissionData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false, unique = true)
		var name: String = ""

) : DataObject<Permission> {
	override fun convert(context: DataObjectContext): Permission {
		return Permission(id, name)
	}

	@ManyToOne(fetch = FetchType.LAZY)
	var profile: ProfileData? = null

}
