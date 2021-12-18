package com.yeet.yeetonline

import com.yeet.yeetonline.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

data class CommentResults(
    val results: List<Comment>,
    val page: Int,
    val size: Int
)

@Controller
class CommentController {

    @Autowired
    lateinit var commentRepo: CommentRepository

    @Secured(USER)
    @ResponseBody
    @GetMapping("/get-comments-by-yeet-id")
    fun getCommentsByYeetId(
        @RequestParam id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): CommentResults {
        val pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending())
        val results =  commentRepo.findCommentsByYeetId(id, pageable)
        return CommentResults(
            results = results.content,
            page = results.pageable.pageNumber,
            size = results.pageable.pageSize
        )
    }

    @Secured(USER)
    @ResponseBody
    @PostMapping("/post-comment")
    fun postComment(
        @RequestParam yeetId: Long,
        @RequestParam content: String
    ): Comment {
        val user = requiredAuthenticatedUser()

        var comment = Comment()
        comment.yeetId = yeetId
        comment.userId = user.id!!
        comment.content = content

        comment = commentRepo.save(comment)

        return comment
    }

    @Secured(USER)
    @ResponseBody
    @DeleteMapping("/delete-comment")
    fun deleteComment(
        @RequestParam id: Long
    ) {
        val user = requiredAuthenticatedUser()
        val comment = commentRepo.findCommentById(id)

        if(user.id != comment.userId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this comment")
        }

        commentRepo.deleteById(id)
    }

    @Secured(USER)
    @ResponseBody
    @PutMapping("/edit-comment")
    fun editComment(
        @RequestParam id: Long,
        @RequestParam content: String
    ): Comment {
        val user = requiredAuthenticatedUser()
        val comment = commentRepo.findCommentById(id)

        if(user.id != comment.userId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to edit this comment")
        }

        comment.content = content
        comment.dateUpdated = Instant.now()
        comment.dateCreated = comment.dateCreated

        var updatedYeet = commentRepo.save(comment)

        return updatedYeet
    }

    @Secured(USER)
    @ResponseBody
    @GetMapping("/get-comment")
    fun getComment(
        @RequestParam id: Long
    ): Comment {
       return commentRepo.findCommentById(id)
    }

}