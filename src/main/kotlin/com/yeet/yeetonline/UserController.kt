package com.yeet.yeetonline

import com.yeet.yeetonline.model.User
import com.yeet.yeetonline.model.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne


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
    ): Any {

        val user = userRepo.findUserByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        if (user.passwordHash != User.hashPassword(password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password failure")
        }

        val userRet = object {
            val id = user.id
            val username = user.username
            val dateCreated = user.dateCreated
            val dateUpdated = user.dateUpdated
        }

        return object {
            val accessToken = getJWTFromUserId(user.id!!)
            val user = userRet
        }
    }

    @Secured(USER)
    @ResponseBody
    @GetMapping("/self")
    fun getSelf(): User {
        val ret = requiredAuthenticatedUser()
        return ret
    }

    @ResponseBody
    @GetMapping("/get-user")
    fun getUser(
        @RequestParam id: Long
    ): User {
        return userRepo.findUserById(id)
    }

    @ResponseBody
    @GetMapping("/get-user-by-username")
    fun getUserByUsername(
        @RequestParam username: String
    ): User? {
        return userRepo.findUserByUsername(username)
    }
}