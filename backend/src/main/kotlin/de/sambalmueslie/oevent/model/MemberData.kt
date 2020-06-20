package de.sambalmueslie.oevent.model

import de.sambalmueslie.oevent.api.Member
import de.sambalmueslie.oevent.common.DataObject
import de.sambalmueslie.oevent.common.DataObjectContext
import javax.persistence.*

@Entity(name = "Member")
@Table(name = "member")
data class MemberData(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		override var id: Long = 0,
		@Column(nullable = false, unique = true)
		var name: String = ""
) : DataObject<Member> {
	override fun convert(context: DataObjectContext): Member {
		return Member(id, name)
	}

	@ManyToOne(fetch = FetchType.LAZY)
	var entitlement: EntitlementData? = null

}
