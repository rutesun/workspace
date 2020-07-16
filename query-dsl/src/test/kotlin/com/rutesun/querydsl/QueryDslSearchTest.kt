package com.rutesun.querydsl

import com.querydsl.jpa.impl.JPAQuery

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RunWith(SpringRunner::class)
@DataJpaTest
class QueryDslSearchTest {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var testEm: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var directorRepository: DirectorRepository
    @Autowired
    private lateinit var movieRepository: MarvelMovieRepository

    @Test
    fun usingQueryDslPredicateExecutor() {
        val russo = Director("Anthony Russo", birthDay = LocalDate.of(1970, 2, 3), country = Country.USA)
        val taika = Director("Taika Waititi", birthDay = LocalDate.of(1975, 8, 16), country = Country.NewZealand)

        val movies = listOf(
            MarvelMovie(name = "Captain America: The Winter Soldier", director = russo, phase = 2).apply { release(LocalDate.of(2014, 4, 4)) },
            MarvelMovie(name = "Avengers: Infinity War", director = russo, phase = 3).apply { release(LocalDate.of(2018, 4, 27)) },
            MarvelMovie(name = "Avengers: Endgame", director = russo, phase = 3).apply { release(LocalDate.of(2019, 4, 26)) },
            MarvelMovie("Thor: Ragnarok", director = taika, phase = 3).apply { release(LocalDate.of(2017, 11, 3)) },
            MarvelMovie("Thor: Love and Thunder", director = taika, phase = 4)
        )

        directorRepository.saveAll(listOf(russo, taika))
        movieRepository.saveAll(movies)

        val qDirector = QDirector.director
        directorRepository.findAll().forEach(::println)
        assertEquals(1, directorRepository.findAll(qDirector.country.eq(Country.USA)).toList().size)

        val qMovie = QMarvelMovie.marvelMovie
        movieRepository.findAll().forEach(::println)
        movieRepository.findAll(qMovie.phase.lt(3)).forEach(::println)
        movieRepository.findAll(qMovie.phase.eq(3).and(qMovie.releaseDate.gt(LocalDate.of(2018,1,1)))).forEach(::println)
    }


}