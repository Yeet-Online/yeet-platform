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
class YeetRepositoryTest {
    @Autowired
    lateinit var yeetRepo: YeetRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @Test
    fun `can create yeet and then find it`() {
        var user = User()
        user.id = null
        user.username = "pizzaTest${Math.random()}"
        user.passwordHash = "wiouhfor"
        user.dateCreated = Instant.now()
        user.dateUpdated = Instant.now()
        user = userRepo.save(user)

        val yeet = Yeet()
        yeet.id = null
        yeet.content = "Hello world!"
        yeet.likes = 0
        yeet.userId = user.id
        yeet.dateCreated = Instant.now()
        yeet.dateUpdated = Instant.now()
        val created = yeetRepo.save(yeet)
        assertNotNull(created)
        assertNotNull(created.id)

        val found = yeetRepo.findById(created.id!!).orElseGet { null }
        assertNotNull(found.id)
        assertEquals(found.id!!, yeet.id!!)
    }
}