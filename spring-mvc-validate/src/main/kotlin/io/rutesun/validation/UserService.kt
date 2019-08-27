package io.rutesun.validation

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Service
@Validated
class UserService(
    val userRepository: UserRepository
) {
    fun createUser(@Valid user: User) {
        userRepository.save(user)
    }
}