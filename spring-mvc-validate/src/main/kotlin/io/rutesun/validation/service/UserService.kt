package io.rutesun.validation.service

import io.rutesun.validation.repository.UserRepository
import io.rutesun.validation.domain.User
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