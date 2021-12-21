package com.yeet.yeetonline.model

import java.io.Serializable
import java.time.Instant
import javax.persistence.*


@Entity
@Table(name = "YEETS")
open class Yeet : Serializable {

    @get:Id
    @get:GeneratedValue(generator = "yeets_id")
    @get:Column(name = "id")
    @get:SequenceGenerator(name="yeets_id", sequenceName = "yeets_id", initialValue = 1, allocationSize = 1)
    var id: Long? = null

    @get:Column(name = "content")
    var content: String = ""

    @get:Column(name = "likes")
    var likes: Long = 0

    @get:Column(name = "date_created")
    var dateCreated: Instant = Instant.now()

    @get:Column(name = "date_updated")
    var dateUpdated: Instant = Instant.now()

    @get:ManyToOne(fetch = FetchType.EAGER)
    @get:JoinColumn(name = "user_id", referencedColumnName = "id")
    lateinit var user: User
}
