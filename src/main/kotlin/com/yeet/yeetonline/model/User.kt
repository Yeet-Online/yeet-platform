package com.yeet.yeetonline.model

import org.springframework.util.DigestUtils
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "USERS")
open class User : Serializable {

    @get:Id
    @get:GeneratedValue(generator = "users_id")
    @get:Column(name = "id")
    @get:SequenceGenerator(name="users_id", sequenceName = "users_id", initialValue = 1, allocationSize = 1)
    var id: Long? = null

    @get:Column(name = "username")
    var username: String = ""

    @get:Column(name = "password_hash")
    var passwordHash: String = ""

    @get:Column(name = "date_created")
    var dateCreated: Instant = Instant.now()

    @get:Column(name = "date_updated")
    var dateUpdated: Instant = Instant.now()

    companion object {
        fun hashPassword(password: String): String = DigestUtils.md5DigestAsHex(password.toByteArray())
    }
}