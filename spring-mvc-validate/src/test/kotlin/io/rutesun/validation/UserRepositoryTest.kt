package io.rutesun.validation

import io.rutesun.validation.domain.User
import io.rutesun.validation.repository.UserRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test(expected = ConstraintViolationException::class)
    fun createUser_fail() {
        val user = User(name = "김", email = "", birthday = LocalDate.now())
        try {
            userRepository.save(user)
        } catch (e: RuntimeException) {
            Assert.assertTrue(e is ConstraintViolationException)
            println(e)
            Assert.assertTrue(e.message!!.contains("email"))
            Assert.assertTrue(e.message!!.contains("name"))
            Assert.assertFalse(e.message!!.contains("birthday"))
            throw e
        }
    }
}