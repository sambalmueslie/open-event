package de.sambalmueslie.openevent.server.member.db

import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.entitlement.api.Entitlement
import de.sambalmueslie.openevent.server.member.api.Member
import de.sambalmueslie.openevent.server.member.api.MemberChangeRequest
import de.sambalmueslie.openevent.server.user.api.User
import javax.persistence.*

@Entity(name = "Member")
@Table(name = "member")
data class MemberData(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	@Column(nullable = false)
	val userId: Long = 0,
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	var entitlement: Entitlement = Entitlement.VIEWER,
	@Column(nullable = false)
	val itemId: Long = 0,
	@Column(nullable = false)
	var contact: Boolean = false
) : DataObject<Member, MemberConvertContent> {
	companion object {
		fun convert(user: User, request: MemberChangeRequest) =
			MemberData(0L, user.id, request.entitlement, request.itemId, request.contact)
	}

	override fun convert(content: MemberConvertContent) = Member(id, content.user, entitlement, itemId, contact)

	fun update(request: MemberChangeRequest) {
		entitlement = request.entitlement
		contact = request.contact
	}
}
