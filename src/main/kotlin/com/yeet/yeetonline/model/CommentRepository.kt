package com.yeet.yeetonline.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: PagingAndSortingRepository<Comment, Long>, JpaRepository<Comment, Long> {
    fun findCommentsByYeetId(id: Long, pageable: Pageable): Page<Comment>
    fun findCommentById(id: Long): Comment
}