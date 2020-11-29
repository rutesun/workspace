package kr.lendit.multiproject.movie

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Studio(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val name: String
) {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "studio")
    val movies: List<Movie> = arrayListOf()

    fun addMovie(movie: Movie): Studio {
        (movies as ArrayList).add(movie)
        return this
    }
}