package com.yeet.yeetonline.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {
    @Autowired
    lateinit var commentRepo: CommentRepository

    @Autowired
    lateinit var yeetRepo: YeetRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @Test
    fun `can create comment and then find it`() {
        var user = User()
        user.id = null
        user.username = "pizzaTest${Math.random()}"
        user.passwordHash = "wiouhfor"
        user.dateCreated = Instant.now()
        user.dateUpdated = Instant.now()
        user = userRepo.save(user)

        var user2 = User()
        user2.id = null
        user2.username = "pizzaTest${Math.random()}"
        user2.passwordHash = "wiouhfor"
        user2.dateCreated = Instant.now()
        user2.dateUpdated = Instant.now()
        user2 = userRepo.save(user2)

        var yeet = Yeet()
        yeet.id = null
        yeet.content = "Hello world!"
        yeet.likes = 0
        yeet.userId = user.id
        yeet.dateCreated = Instant.now()
        yeet.dateUpdated = Instant.now()
        yeet = yeetRepo.save(yeet)

        val comment = Comment()
        comment.id = null
        comment.content = "Hello world!"
        comment.likes = 0
        comment.userId = user2.id
        comment.yeetId = yeet.id
        comment.dateCreated = Instant.now()
        comment.dateUpdated = Instant.now()
        val created = commentRepo.save(comment)
        assertNotNull(created)
        assertNotNull(created.id)

        val found = commentRepo.findById(created.id!!).orElseGet { null }
        assertNotNull(found.id)
        assertEquals(found.id!!, comment.id!!)
    }

    @Test
    fun `can find comments by yeet id`() {
        var user = User()
        user.id = null
        user.username = "pizzaTest${Math.random()}"
        user.passwordHash = "wiouhfor"
        user.dateCreated = Instant.now()
        user.dateUpdated = Instant.now()
        user = userRepo.save(user)

        var yeet = Yeet()
        yeet.id = null
        yeet.content = "Hello world!"
        yeet.likes = 0
        yeet.userId = user.id
        yeet.dateCreated = Instant.now()
        yeet.dateUpdated = Instant.now()
        yeet = yeetRepo.save(yeet)

        for (i in 0 until 50){
            var comment = Comment()
            comment.id = null
            comment.content = "I am a comment ${Math.random()}"
            comment.likes = 0
            comment.userId = user.id
            comment.yeetId = yeet.id
            comment.dateCreated = Instant.now()
            comment.dateUpdated = Instant.now()
            comment = commentRepo.save(comment)
        }

        val page = commentRepo.findCommentsByYeetId(yeet.id!!, Pageable.ofSize(10))
        assertEquals(10, page.size)

    }
}