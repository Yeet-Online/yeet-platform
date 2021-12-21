package com.yeet.yeetonline.model

import java.io.Serializable
import java.time.Instant
import javax.persistence.*

open class UserFollowId : Serializable {
    var sourceUserId: Long = -1
    var targetUserId: Long = -1

    constructor(sourceUserId: Long, targetUserId: Long) {
        this.sourceUserId = sourceUserId
        this.targetUserId = targetUserId
    }

    constructor()
}

@Entity
@Table(name = "USER_FOLLOWS")
@IdClass(UserFollowId::class)
open class UserFollow : Serializable {

    @get:Id
    @get:Column(name = "source_user_id")
    var sourceUserId: Long = -1

    @get:Id
    @get:Column(name = "target_user_id")
    var targetUserId: Long = -1

    @get:Column(name = "date_created")
    var dateCreated: Instant = Instant.now()
}