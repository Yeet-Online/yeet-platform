package com.yeet.yeetonline.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserFollowRepository: PagingAndSortingRepository<UserFollow, UserFollowId>, JpaRepository<UserFollow, UserFollowId> {
    fun findBySourceUserIdOrderByDateCreatedDesc(sourceUserId: Long, pageable: Pageable): Page<UserFollow>
    fun findByTargetUserIdOrderByDateCreatedDesc(targetUserId: Long, pageable: Pageable): Page<UserFollow>
}