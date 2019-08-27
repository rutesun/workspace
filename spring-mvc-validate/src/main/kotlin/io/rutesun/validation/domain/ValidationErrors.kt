package io.rutesun.validation.domain

import java.util.ArrayList

class ValidationErrors {
    val errors: MutableList<Violation> = ArrayList()
    fun add(violation: Violation) = this.errors.add(violation)
}

data class Violation(val fieldName: String, val message: String?)