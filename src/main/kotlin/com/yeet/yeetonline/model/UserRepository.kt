package com.yeet.yeetonline.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {
    fun findUserByUsername(username: String): User?
    fun findUserById(id: Long): User
}