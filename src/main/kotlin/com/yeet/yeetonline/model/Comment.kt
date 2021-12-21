package com.yeet.yeetonline.model

import java.io.Serializable
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "COMMENTS")
open class Comment : Serializable {

    @get:Id
    @get:GeneratedValue(generator = "comments_id")
    @get:Column(name = "id")
    @get:SequenceGenerator(name="comments_id", sequenceName = "comments_id", initialValue = 1, allocationSize = 1)
    var id: Long? = null

    @get:ManyToOne(fetch = FetchType.EAGER)
    @get:JoinColumn(name = "user_id", referencedColumnName = "id")
    lateinit var user: User

    @get:Column(name = "yeet_id")
    var yeetId: Long? = null

    @get:Column(name = "content")
    var content: String = ""

    @get:Column(name = "likes")
    var likes: Long = 0

    @get:Column(name = "date_created")
    var dateCreated: Instant = Instant.now()

    @get:Column(name = "date_updated")
    var dateUpdated: Instant = Instant.now()
}