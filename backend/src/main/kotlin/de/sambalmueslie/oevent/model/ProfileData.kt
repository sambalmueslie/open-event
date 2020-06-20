package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Profile
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import javax.persistence.*

@Entity(name = "Profile")
@Table(name = "profile")
data class ProfileData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false, unique = true)
		var name: String = ""
) : DataObject<Profile> {
	override fun convert(context: DataObjectContext): Profile {
		return Profile(id, name)
	}

	val permissions: Set<PermissionData>
		get() = _permissions

	@OneToMany(mappedBy = "profile", cascade = [CascadeType.ALL], orphanRemoval = true)
	private val _permissions: MutableSet<PermissionData> = mutableSetOf()

	fun add(permission: PermissionData) {
		_permissions.add(permission)
		permission.profile = this
	}

	fun remove(permission: PermissionData) {
		_permissions.remove(permission)
		permission.profile = null
	}

	val entitlements: Set<EntitlementData>
		get() = _entitlements

	@OneToMany(mappedBy = "profile", cascade = [CascadeType.ALL], orphanRemoval = true)
	private val _entitlements: MutableSet<EntitlementData> = mutableSetOf()

	fun add(entitlement: EntitlementData) {
		_entitlements.add(entitlement)
		entitlement.profile = this
	}

	fun remove(entitlement: EntitlementData) {
		_entitlements.remove(entitlement)
		entitlement.profile = null
	}
}
