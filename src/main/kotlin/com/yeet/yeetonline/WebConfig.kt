package com.yeet.yeetonline

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.web.firewall.StrictHttpFirewall
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
class WebConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var rememberMeServices: YeetRememberMeService

    override fun configure(web: WebSecurity) {
        val firewall = StrictHttpFirewall()
        firewall.setAllowUrlEncodedSlash(true)
        firewall.setAllowUrlEncodedPercent(true)

        web
            .httpFirewall(firewall)
            .ignoring()
            .antMatchers("/health")
            .antMatchers("/actuator/health")
            .antMatchers("/health/**")
            .antMatchers("/actuator/health/**")
            .antMatchers("/error")
            .antMatchers("/error/**")
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeRequests {
                it.antMatchers("/actuator/health").permitAll()
                    .anyRequest().permitAll()
            }
            .rememberMe {
                it.rememberMeServices(rememberMeServices)
            }
            .authenticationProvider(YeetAuthProvider())
            .exceptionHandling()
    }

    @Bean
    fun corsFilter(): FilterRegistrationBean<CorsFilter> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:4200")
        config.addAllowedOrigin("http://localhost:3000")
        config.addAllowedHeader("*")
        config.allowedMethods = listOf(
            "GET", "OPTIONS", "POST", "PUT", "DELETE", "PATCH"
        )
        config.allowedOriginPatterns = listOf("*")
        source.registerCorsConfiguration("/**", config)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0
        return bean
    }
}

@Component
class YeetAuthProvider : AuthenticationProvider {
    override fun supports(authentication: Class<*>?): Boolean = YeetAuthentication::class.java.isAssignableFrom(authentication)
    override fun authenticate(authentication: Authentication): Authentication {
        return authentication
    }
}
