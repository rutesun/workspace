package io.rutesun.validation

import org.hibernate.validator.constraints.Length
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PastOrPresent

@Entity
data class User(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:Length(min = 2)
    @field:NotBlank
    val name: String,

    @get:DateTimeFormat(pattern = "yyyyMMdd")
    @field:PastOrPresent
    val birthday: LocalDate
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}



