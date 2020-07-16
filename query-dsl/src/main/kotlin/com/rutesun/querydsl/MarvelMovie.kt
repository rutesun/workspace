package com.rutesun.querydsl

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

enum class MovieStatus {
    PreProduction,
    Production,
    PostProduction,
    Distribution
}

@Entity
data class MarvelMovie(
    val name: String,
    @ManyToOne
    @JoinColumn(name = "director_id")
    val director: Director,
    val phase: Int,
    var description: String? = null
) {
    var status: MovieStatus = MovieStatus.PreProduction

    @Id
    @GeneratedValue
    val id: Long = 0

    private var releaseDate: LocalDate? = null

    fun release(date: LocalDate) {
        this.status = MovieStatus.Distribution
        this.releaseDate = date
    }
}

@Repository
interface MarvelMovieRepository : JpaRepository<MarvelMovie, Long>, QuerydslPredicateExecutor<MarvelMovie>

