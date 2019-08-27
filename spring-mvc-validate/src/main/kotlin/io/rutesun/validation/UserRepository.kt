package io.rutesun.validation

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Number>