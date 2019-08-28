package io.rutesun.validation.service

import io.rutesun.validation.domain.Address
import io.rutesun.validation.domain.OnCreate
import io.rutesun.validation.domain.OnUpdate
import io.rutesun.validation.repository.UserRepository
import io.rutesun.validation.domain.User
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.groups.Default

@Service
@Validated
class UserService {

    fun createUser(@Email @NotEmpty email: String, @NotEmpty name: String, @Valid address: Address) {
    }

    @Validated(Default::class, OnCreate::class)
    fun createUser(@Valid user: User) {
    }

    fun createUserPassValidation(user: User) {
    }

    @Validated(Default::class, OnUpdate::class)
    fun updateUser(@Valid user: User) {
    }
}