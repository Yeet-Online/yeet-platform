package com.yeet.yeetonline

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

const val HEADER_ACCESS_TOKEN = "Authorization"

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .securitySchemes(listOf(apiKey()))
            .securityContexts(listOf(securityContext()))
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Product REST API",
            "Product API to perform CRUD opertations",
            "1.0",
            "Terms of service",
            Contact("Java Chinna", "www.javachinna.com", "java4chinna@gmail.com"),
            "License of API",
            "API license URL",
            emptyList()
        )
    }

    private fun apiKey(): ApiKey? {
        return ApiKey(HEADER_ACCESS_TOKEN, HEADER_ACCESS_TOKEN, "header")
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/.*"))
            .build()
    }

    private fun defaultAuth(): List<SecurityReference>? {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference(HEADER_ACCESS_TOKEN, authorizationScopes))
    }
}