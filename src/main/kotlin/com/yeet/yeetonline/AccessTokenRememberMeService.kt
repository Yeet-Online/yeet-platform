package com.yeet.yeetonline

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.yeet.yeetonline.model.User
import com.yeet.yeetonline.model.UserRepository
import com.yeet.yeetonline.model.Yeet
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val AUTHORIZATION_HEADER = "Authorization"
const val PARAM_ACCESS_TOKEN = "accessToken"
const val YEET_SECRET = "This is super top secret"
const val YEET_ISSUER = "Yeet"
const val USER = "ROLE_USER"
val USER_AUTH = SimpleGrantedAuthority(USER)

fun getAuthenticatedUser(): User? {
    return SecurityContextHolder.getContext().authentication
        .takeIf { it is YeetAuthentication }
        ?.let { it as YeetAuthentication }
        ?.user
}

fun requiredAuthenticatedUser(): User = requireNotNull(getAuthenticatedUser()) { "No Authenticated User found"}

fun getUserIdFromJWT(token: String) = JWT
    .require(Algorithm.HMAC256(YEET_SECRET))
    .withIssuer(YEET_ISSUER)
    .build()
    .verify(token)
    .claims["userId"]?.asLong()

fun getJWTFromUserId(userId: Long): String  = JWT.create()
    .withClaim("userId", userId)
    .withIssuer(YEET_ISSUER)
    .sign(Algorithm.HMAC256(YEET_SECRET))

class YeetAuthentication(
    val user: User,
    additionalAuthorities: Collection<GrantedAuthority> = emptySet()
) : AbstractAuthenticationToken(setOf(USER_AUTH) + additionalAuthorities) {
    init {
        isAuthenticated = true
    }
    override fun getCredentials(): Any  = user
    override fun getPrincipal(): Any = user
}

@Component
class YeetRememberMeService(
    private val userRepo: UserRepository
) : RememberMeServices {

    override fun autoLogin(request: HttpServletRequest, response: HttpServletResponse): Authentication? {

        var encodedAccessToken = request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.matches(Regex("^$PARAM_ACCESS_TOKEN\\s*=\\s*.*$")) }
            ?.let { it.substring(it.indexOf("=") + 1) }
            ?.trim()
            ?.takeIf { it.isNotEmpty() }

        if (encodedAccessToken == null) {
            encodedAccessToken = request.getHeader(AUTHORIZATION_HEADER)
                ?.let { Regex("^Bearer\\s*(.*)$").find(it) }
                ?.let { it.groupValues[1] }
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
        }

        if (encodedAccessToken == null) {
            encodedAccessToken = request.getHeader(HEADER_ACCESS_TOKEN)
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
        }

        if (encodedAccessToken == null) {
            encodedAccessToken = request.getParameter(PARAM_ACCESS_TOKEN)
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
        }

        if (encodedAccessToken == null) {
            return null
        }

        val userId = getUserIdFromJWT(encodedAccessToken)
            ?: return null

        val user = userRepo.findById(userId).orElse(null)
            ?: return null

        val grants = mutableSetOf<GrantedAuthority>()
        return YeetAuthentication(user, grants)
    }

    override fun loginFail(request: HttpServletRequest, response: HttpServletResponse) {}
    override fun loginSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        successfulAuthentication: Authentication
    ) { }
}