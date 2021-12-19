package com.yeet.yeetonline.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface YeetRepository: PagingAndSortingRepository<Yeet, Long>, JpaRepository<Yeet, Long> {
    fun findYeetsByUserId(userId: Long, pageable: Pageable): Page<Yeet>
    fun findYeetById(id: Long): Yeet
    fun findYeetByUser_Username(username: String, pageable: Pageable): Page<Yeet>
}