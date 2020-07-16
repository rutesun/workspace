package com.rutesun.querydsl

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.LinkedList
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Director(
    var name: String,
    val birthDay: LocalDate,
    val country: Country
) {
    @Id
    @GeneratedValue
    val id: Long = 0

    @OneToMany(mappedBy = "director")
    val films: MutableList<MarvelMovie> = LinkedList()
}

@Repository
interface DirectorRepository : JpaRepository<Director, Long>, QuerydslPredicateExecutor<Director>