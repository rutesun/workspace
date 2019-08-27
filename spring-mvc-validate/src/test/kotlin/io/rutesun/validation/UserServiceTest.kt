package io.rutesun.validation

import io.rutesun.validation.domain.Address
import io.rutesun.validation.domain.User
import io.rutesun.validation.service.UserService
import io.rutesun.validation.service.UserServiceWithValidator
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.validation.SmartValidator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.LocalDate
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userServiceWithValidator: UserServiceWithValidator

    @Test
    fun validator() {
        Assert.assertTrue(userServiceWithValidator.validator is LocalValidatorFactoryBean)
        Assert.assertTrue(userServiceWithValidator.validator is SmartValidator)
    }

    @Test(expected = ConstraintViolationException::class)
    fun createUser_fail() {
        val user = User(name = "김", email = "", birthday = LocalDate.now().plusDays(1))
        try {
            userService.createUser(user)
        } catch (e: RuntimeException) {
            Assert.assertTrue(e is ConstraintViolationException)
            println(e)
            Assert.assertTrue(e.message!!.contains("email"))
            Assert.assertTrue(e.message!!.contains("name"))
            Assert.assertTrue(e.message!!.contains("birthday"))
            throw e
        }
    }

    @Test(expected = ConstraintViolationException::class)
    fun createUser_fail2() {
        val user = User(name = "김", email = "", birthday = LocalDate.now())
        try {
            userServiceWithValidator.createUser(user)
        } catch (e: RuntimeException) {
            Assert.assertTrue(e is ConstraintViolationException)
            println(e)
            Assert.assertTrue(e.message!!.contains("email"))
            Assert.assertTrue(e.message!!.contains("name"))
            Assert.assertFalse(e.message!!.contains("birthday"))
            throw e
        }
    }

    @Test(expected = ConstraintViolationException::class)
    fun createUserWithAddress() {
        val user = User(name = "김", email = "", birthday = LocalDate.now(), address = Address(zipCode = "1", address = ""))
        try {
            userServiceWithValidator.createUser(user)
        } catch (e: RuntimeException) {
            Assert.assertTrue(e is ConstraintViolationException)
            println(e)
            Assert.assertTrue(e.message!!.contains("email"))
            Assert.assertTrue(e.message!!.contains("name"))
            Assert.assertFalse(e.message!!.contains("birthday"))
            throw e
        }
    }

    @Test
    fun createUser() {
        val user = User(name = "홍길동", email = "honggildong@email.com", birthday = LocalDate.now().minusDays(1))
        userService.createUser(user)
    }
}