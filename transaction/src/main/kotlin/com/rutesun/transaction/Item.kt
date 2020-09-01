package com.rutesun.transaction

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Item(
    @Id
    @GeneratedValue
    val id: Long = 0L,
    val amount: Int,
    @Column(length = 20)
    val name: String
)