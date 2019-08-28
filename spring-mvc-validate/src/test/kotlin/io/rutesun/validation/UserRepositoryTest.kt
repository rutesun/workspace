package io.rutesun.validation

import io.rutesun.validation.domain.User
import io.rutesun.validation.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import javax.validation.ConstraintViolationException

@ExtendWith(SpringExtension::class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    private fun getInvalidUser() = User(name = "김", email = "", birthday = LocalDate.now().plusDays(1))

    @Test
    fun createUser_fail() {
        val user = getInvalidUser()

        val exception = assertThrows<ConstraintViolationException> {
            userRepository.save(user)
        }

        Assertions.assertTrue(exception.message!!.contains("email"))
        Assertions.assertTrue(exception.message!!.contains("name"))
        Assertions.assertTrue(exception.message!!.contains("birthday"))
    }
}