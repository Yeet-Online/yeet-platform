package com.yeet.yeetonline.model

import org.flywaydb.test.FlywayTestExecutionListener
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import java.time.Instant

@ExtendWith(SpringExtension::class)
@DataJdbcTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    lateinit var userRepo: UserRepository

    @Test
    fun `can create user`() {
        val user = User(
            id = null,
            username = "testUser",
            passwordHash = "wiouhfor",
            dateCreated = Instant.now(),
            dateUpdated = Instant.now()
        )
        val created = userRepo.save(user)
        assertNotNull(created)
    }
}