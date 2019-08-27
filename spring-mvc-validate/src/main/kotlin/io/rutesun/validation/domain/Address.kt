package io.rutesun.validation.domain

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty

data class Address(
    @field:Length(min = 5, max = 6)
    @field:NotEmpty
    val zipCode: String,
    @field:Length(max = 255)
    @field:NotEmpty
    val address: String
)
