package kr.lendit.multiproject.movie

import javax.persistence.CascadeType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId

//@Entity
class FilmoGraphy(
    @Id
    @GeneratedValue
    val id: Long = 0,
    @ManyToOne(cascade = [CascadeType.ALL])
    @MapsId("actor_id")
    @JoinColumn(name = "actor_id")
    val actor: Actor,
    @ManyToOne(cascade = [CascadeType.ALL])
    @MapsId("movie_id")
    @JoinColumn(name = "movie_id")
    val movie: Movie
)
