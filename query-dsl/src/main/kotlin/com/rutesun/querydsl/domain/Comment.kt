package com.rutesun.querydsl

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Comment(
    val text: String,
    val isSecret: Boolean = false,
    val postId: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
