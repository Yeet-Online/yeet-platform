package com.yeet.yeetonline

import com.yeet.yeetonline.model.User
import com.yeet.yeetonline.model.UserRepository
import com.yeet.yeetonline.model.Yeet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.server.ResponseStatusException

@Controller
class UserController {

    @Autowired
    lateinit var userRepo: UserRepository

    @ResponseBody
    @PostMapping("/register")
    fun registerUser(
        @RequestParam username: String,
        @RequestParam password: String
    ): User {

        var user = userRepo.findUserByUsername(username)
        if (user != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "User exists")
        }

        user = User()
        user.username = username
        user.passwordHash = User.hashPassword(password)
        user = userRepo.save(user)
        return user
    }

    @ResponseBody
    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): String {

        val user = userRepo.findUserByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        if (user.passwordHash != User.hashPassword(password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password failure")
        }

        return getJWTFromUserId(user.id!!)
    }

    @Secured(USER)
    @ResponseBody
    @PostMapping("/self")
    fun getSelf(): User {
        val ret = requiredAuthenticatedUser()
        return ret
    }
}