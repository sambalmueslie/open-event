package de.sambalmueslie.openevent.server.announcement.db

import de.sambalmueslie.openevent.server.announcement.api.Announcement
import de.sambalmueslie.openevent.server.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.server.common.DataObject
import de.sambalmueslie.openevent.server.user.api.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "Announcement")
@Table(name = "announcement")
data class AnnouncementData(
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	override var id: Long = 0L,
	@Column(nullable = false)
	var subject: String = "",
	@Column(columnDefinition = "TEXT", nullable = false)
	var content: String = "",
	@Column(nullable = false)
	val authorId: Long = 0L,
	@Column(nullable = false)
	val itemId: Long = 0L,
	@Column(nullable = false)
	val created: LocalDateTime = LocalDateTime.now(),
	@Column(nullable = false)
	var modified: LocalDateTime = LocalDateTime.now(),
) : DataObject<Announcement, AnnouncementConvertContent> {

	companion object {
		fun convert(user: User, request: AnnouncementChangeRequest): AnnouncementData {
			return AnnouncementData(0L, request.subject, request.content, user.id, request.itemId)
		}
	}

	override fun convert(content: AnnouncementConvertContent): Announcement {
		return Announcement(id, subject, this.content, content.author, created, itemId)
	}

	fun update(request: AnnouncementChangeRequest) {
		subject = request.subject
		content = request.content
		modified = LocalDateTime.now()
	}
}
