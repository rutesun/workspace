package kr.lendit.multiproject.movie

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import javax.persistence.EntityManager

@Service
@Transactional
class MovieService(
    private val em: EntityManager,
    private val castingService: CastingService
) {
    fun create(name: String, year: Year, description: String? = null): Movie {
        val movie = Movie(name = name, madeYear = year.value, description = description)
        return movie.apply { em.persist(movie) }
    }

    fun casting(movieId: Int, actors: Set<Actor>) {
        val movie = em.find(Movie::class.java, movieId)
        castingService.casting(movie, actors)
    }
}