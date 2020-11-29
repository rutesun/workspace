package com.rutesun.querydsl

import com.rutesun.querydsl.MarvelMovie
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Actor(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val name: String,
    val country: String,
    val birth: LocalDate
) {
//    @OneToMany(mappedBy = "actor", cascade = [CascadeType.ALL])
//    @ManyToMany(mappedBy = "actors")
//    val filmoGraphys: List<MarvelMovie> = arrayListOf()

//    fun addFilmography(movie: MarvelMovie): Actor {
//        (filmoGraphys as ArrayList).add(movie)
//        return this
//    }
}