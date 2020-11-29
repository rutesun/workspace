package com.rutesun.jpa.movie

import java.time.LocalDate
import javax.persistence.Entity

@Entity
data class Director (
    var name: String,
    val birthDay: LocalDate
)