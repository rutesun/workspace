package io.rutesun.validation.repository

import io.rutesun.validation.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Number>