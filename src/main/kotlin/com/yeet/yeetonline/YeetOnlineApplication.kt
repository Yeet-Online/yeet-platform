package com.yeet.yeetonline

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@EnableJdbcRepositories
@SpringBootApplication
class YeetOnlineApplication

fun main(args: Array<String>) {
	runApplication<YeetOnlineApplication>(*args)
}
