package com.yeet.yeetonline.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("USER")
data class User(
    @Id
    val id: Long? = null,
    val username: String,
    val passwordHash: String,
    val dateCreated: Instant,
    val dateUpdated: Instant
)