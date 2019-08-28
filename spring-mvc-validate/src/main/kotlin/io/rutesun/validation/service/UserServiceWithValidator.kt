package io.rutesun.validation.service

import io.rutesun.validation.domain.Address
import io.rutesun.validation.repository.UserRepository
import io.rutesun.validation.domain.User
import org.springframework.stereotype.Service
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.Validator
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Service
class UserServiceWithValidator(
    val validator: Validator,
    val userRepository: UserRepository
) {
    fun createUser(@Email @NotEmpty email: String, @NotEmpty name: String, @Valid address: Address) {
    }

    fun createUser(user: User) {
        val validate = validator.validate(user)
        if (validate.isNotEmpty()) {
            throw ConstraintViolationException(validate)
        }
        userRepository.save(user)
    }
}