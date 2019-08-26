package io.rutesun.validation

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PastOrPresent

data class User(
    @NotBlank
    @Email
    val email: String,

    @NotBlank
    val name: String,

    @NotEmpty
    val password: String,

    @DateTimeFormat(pattern = "yyyyMMdd")
    @PastOrPresent
    val birthday: LocalDate
)





