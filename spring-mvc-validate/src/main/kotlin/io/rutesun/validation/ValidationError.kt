package io.rutesun.validation

import java.util.ArrayList

class ValidationError {
    val errors: MutableList<Violation> = ArrayList()
    fun add(violation: Violation) = this.errors.add(violation)
}

data class Violation(val fieldName: String, val message: String?)