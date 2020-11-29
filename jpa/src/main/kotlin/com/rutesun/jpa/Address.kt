package com.rutesun.jpa

import javax.persistence.Embeddable

@Embeddable
data class Address(
    val zipCode: String,
    val detail: String
)

