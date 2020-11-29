package kr.lendit.multiproject.movie

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
class CastingService(
    private val em: EntityManager
) {
    fun casting(movie: Movie, actors: Set<Actor>) {
        movie.casting(actors = *actors.toTypedArray())
        em.persist(movie)
    }
}