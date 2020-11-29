package kr.lendit.multiproject.movie

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
data class Movie(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val name: String,
    val madeYear: Int,
    var description: String? = null
) {

    @Column(name = "theater_id")
    var theaterId: Long = 0

    @ManyToOne
    @JoinColumn(name = "studio_id")
    lateinit var studio: Studio

//    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL])
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "filmography",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "actor_id")]
    )
    val actors: List<Actor> = arrayListOf()

    fun casting(actor: Actor): Movie {
        (actors as ArrayList).add(actor)
        return this
    }

    fun casting(vararg actors: Actor): Movie {
        actors.forEach { casting(it) }
        return this
    }
}
