package com.yeet.yeetonline

import com.yeet.yeetonline.model.User
import com.yeet.yeetonline.model.UserRepository
import com.yeet.yeetonline.model.Yeet
import com.yeet.yeetonline.model.YeetRepository
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

data class YeetResults(
    val results: List<Yeet>,
    val page: Int,
    val size: Int
)

@Controller
class YeetController {

    @Autowired
    lateinit var yeetRepo: YeetRepository

    @ResponseBody
    @GetMapping("/explore-feed")
    fun getExploreFeed(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): YeetResults {
        val pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending())
        val results =  yeetRepo.findAll(pageable)
        return YeetResults(
            results = results.content,
            page = results.pageable.pageNumber,
            size = results.pageable.pageSize
        )
    }

    @Secured(USER)
    @ResponseBody
    @PostMapping("/post-yeet")
    fun postYeet(
        @RequestParam content: String
    ): Yeet {
        val user = requiredAuthenticatedUser()

        var yeet = Yeet()
        yeet.userId = user.id!!
        yeet.content = content

        yeet = yeetRepo.save(yeet)

        return yeet
    }

    @Secured(USER)
    @ResponseBody
    @DeleteMapping("/delete-yeet")
    fun deleteYeet(
        @RequestParam id: Long
    ) {
        val user = requiredAuthenticatedUser()
        val yeet = yeetRepo.findYeetById(id)

        if(user.id != yeet.userId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this post")
        }

        yeetRepo.deleteById(id)
    }

    @Secured(USER)
    @ResponseBody
    @PutMapping("/edit-yeet")
    fun editYeet(
        @RequestParam id: Long,
        @RequestParam content: String
    ): Yeet {
        val user = requiredAuthenticatedUser()
        val yeet = yeetRepo.findYeetById(id)

        if(user.id != yeet.userId) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to edit this post")
        }

        yeet.content = content
        yeet.dateUpdated = Instant.now()
        yeet.dateCreated = yeet.dateCreated

        var updatedYeet = yeetRepo.save(yeet)

        return updatedYeet
    }

    @Secured(USER)
    @ResponseBody
    @GetMapping("/get-yeet")
    fun getYeet(
        @RequestParam id: Long
    ): Yeet {
       return yeetRepo.findYeetById(id)
    }

}