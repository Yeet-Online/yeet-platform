package com.yeet.yeetonline.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@DataJpaTest
@ActiveProfiles("test")
class UserFollowRepositoryTest {
    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var userFollowRepo: UserFollowRepository

    private fun createUser(): User {
        val user = User()
        user.id = null
        user.username = "pizzaTest${Math.random()}"
        user.passwordHash = "wiouhfor"
        user.dateCreated = Instant.now()
        user.dateUpdated = Instant.now()
        return userRepo.save(user)
    }

    @Test
    fun `can follow user`() {
        val user1 = createUser()
        val user2 = createUser()
        val user3 = createUser()

        var followers = userFollowRepo.findBySourceUserIdOrderByDateCreatedDesc(user1.id!!, Pageable.unpaged())
        assertTrue(followers.isEmpty)
        followers = userFollowRepo.findBySourceUserIdOrderByDateCreatedDesc(user2.id!!, Pageable.unpaged())
        assertTrue(followers.isEmpty)
        followers = userFollowRepo.findBySourceUserIdOrderByDateCreatedDesc(user3.id!!, Pageable.unpaged())
        assertTrue(followers.isEmpty)

        val follow = UserFollow()
        follow.sourceUserId = user1.id!!
        follow.targetUserId = user2.id!!
        follow.dateCreated = Instant.now()
        val x = userFollowRepo.save(follow)
        val y = userFollowRepo.findAll()
        followers = userFollowRepo.findBySourceUserIdOrderByDateCreatedDesc(user1.id!!, Pageable.unpaged())
        assertEquals(1, followers.size)
        assertEquals(user1.id, followers.content[0].sourceUserId)
        assertEquals(user2.id, followers.content[0].targetUserId)
    }

    @Test
    fun `cant follow myself`() {
        val user1 = createUser()

        val followers = userFollowRepo.findBySourceUserIdOrderByDateCreatedDesc(user1.id!!, Pageable.unpaged())
        assertTrue(followers.isEmpty)

        val follow = UserFollow()
        follow.sourceUserId = user1.id!!
        follow.targetUserId = user1.id!!
        try {
            userFollowRepo.save(follow)
            fail("Was able to follow myself")
        } catch (t: Throwable) {
            // good
        }
    }
}