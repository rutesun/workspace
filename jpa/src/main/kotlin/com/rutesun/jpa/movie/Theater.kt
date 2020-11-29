package kr.lendit.multiproject.movie

import com.rutesun.jpa.Address
import javax.persistence.CascadeType
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
data class Theater (
    val name: String,
    @Embedded
    val address: Address,
    @Id
    @GeneratedValue
    val id: Long = 0
) {
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(referencedColumnName = "theater_id")
    var movie: Movie? = null
}