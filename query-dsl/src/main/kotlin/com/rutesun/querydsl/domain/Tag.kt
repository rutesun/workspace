package com.rutesun.querydsl.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Tag(
    val label: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) {
    @ManyToMany(mappedBy = "tags")
    val posts: List<Post> = mutableListOf()
}