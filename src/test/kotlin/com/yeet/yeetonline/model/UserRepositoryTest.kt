package com.yeet.yeetonline.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    lateinit var userRepo: UserRepository

    @Test
    fun `can create user and then find it`() {
        val user = User()
        user.id = null
        user.username = "pizzaTest${Math.random()}"
        user.passwordHash = "wiouhfor"
        user.dateCreated = Instant.now()
        user.dateUpdated = Instant.now()
        val created = userRepo.save(user)
        assertNotNull(created)
        assertNotNull(created.id)

        val found = userRepo.findById(created.id!!).orElseGet { null }
        assertNotNull(found.id)
        assertEquals(found.id!!, user.id!!)
    }
}