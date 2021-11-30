package com.yeet.yeetonline.model

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.event.BeforeSaveEvent
import org.springframework.stereotype.Repository
import java.awt.print.Book
import java.util.*

@Repository
interface UserRepository: PagingAndSortingRepository<User, String> {
}