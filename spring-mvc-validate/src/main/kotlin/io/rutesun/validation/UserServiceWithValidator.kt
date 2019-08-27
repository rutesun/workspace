package io.rutesun.validation

import org.springframework.stereotype.Service
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.Validator

@Service
class UserServiceWithValidator(
    val validator: Validator,
    val userRepository: UserRepository
) {
    fun createUser(@Valid user: User) {
        val validate = validator.validate(user)
        if (validate.isNotEmpty()) {
            throw ConstraintViolationException(validate)
        }
        userRepository.save(user)
    }
}