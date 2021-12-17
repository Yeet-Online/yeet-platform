package com.yeet.yeetonline

import com.yeet.yeetonline.model.User
import com.yeet.yeetonline.model.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.client.WebClient

@Controller
class AuthenticationController {

    @Autowired
    lateinit var userRepo: UserRepository

    @ResponseBody
    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String
    ): User {

        val user = userRepo.findUserByUsername(username)
            ?: throw HttpServerErrorException(HttpStatus.NOT_FOUND, "User not found")

        if (user.passwordHash != User.hashPassword(password)) {
            throw HttpServerErrorException(HttpStatus.UNAUTHORIZED, "Password failure")
        }

        return user
    }

    @PostMapping("/login-with-github")
    fun loginWithGithub(
        @RequestParam userInfoUrl: String,
        @RequestParam accessToken: String
    ): String {

        val userInfo = WebClient.create(userInfoUrl)
            .get()
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer $accessToken"
            )
            .retrieve()
            .bodyToMono(MutableMap::class.java)
            .block() ?: emptyMap()

        val login = userInfo["login"].toString()

        println(userInfo)

        // TODO: find user by login
        // TODO: if user doesn't exist, create one for the login
        // TODO: redirect them tp their profile page

        return "ok"
    }
}