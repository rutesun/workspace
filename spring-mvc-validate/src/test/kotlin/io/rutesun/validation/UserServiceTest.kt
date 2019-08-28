package io.rutesun.validation

import io.rutesun.validation.domain.Address
import io.rutesun.validation.domain.User
import io.rutesun.validation.service.UserService
import io.rutesun.validation.service.UserServiceWithValidator
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.SmartValidator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.LocalDate
import javax.validation.ConstraintViolationException

@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userServiceWithValidator: UserServiceWithValidator

    @DisplayName("Spring Default validator 확인")
    @Test
    fun validator() {
        assertTrue(userServiceWithValidator.validator is LocalValidatorFactoryBean)
        assertTrue(userServiceWithValidator.validator is SmartValidator)
    }

    private fun getInvalidUser() = User(name = "김", email = "", birthday = LocalDate.now().plusDays(1))
    private fun getValidUser() = User(name = "홍길동", email = "honggildong@email.com", birthday = LocalDate.now().minusDays(1))

    @DisplayName("유저 생성시 AOP를 통한 유효성 검사")
    @Test
    fun createUser_fail() {
        val user = getInvalidUser().apply { id = 1 }
        val exception = assertThrows<ConstraintViolationException> {
            userService.createUser(user)
        }

        userService.createUser(user)
        assertTrue(exception.message!!.contains("email"))
        assertTrue(exception.message!!.contains("name"))
        assertTrue(exception.message!!.contains("birthday"))
    }

    @DisplayName("유저 생성시 validator 를 통한 검사")
    @Test
    fun createUser_fail2() {
        val user = getInvalidUser()
        val exception = assertThrows<ConstraintViolationException> {
            userServiceWithValidator.createUser(user)
        }

        assertTrue(exception.message!!.contains("email"))
        assertTrue(exception.message!!.contains("name"))
        assertTrue(exception.message!!.contains("birthday"))
    }

    @DisplayName("유저 생성시 AOP를 통한 유효성 검사")
    @Test
    fun createUser_fail3() {
        val exception = assertThrows<ConstraintViolationException> {
            userService.createUser("", "", address = Address("1", "1"))
        }

        assertDoesNotThrow {
            userServiceWithValidator.createUser("", "", address = Address("1", "1"))
        }

        assertTrue(exception.message!!.contains("email"))
        assertTrue(exception.message!!.contains("name"))
        assertTrue(exception.message!!.contains("zipCode"))
        assertTrue(exception.message!!.contains("address"))
    }

    @DisplayName("유저 생성시 validate annotation 이 없는 경우")
    @Test
    fun createUser_fail4() {
        val user = getInvalidUser()
        userService.createUserPassValidation(user)
    }

    @DisplayName("중첩된 클래스의 유효성 검사")
    @Test
    fun createUserWithAddress() {
        val user = getInvalidUser().copy(address = Address(zipCode = "1", address = ""))
        val exception = assertThrows<ConstraintViolationException> {
            userServiceWithValidator.createUser(user)
        }

        assertTrue(exception.message!!.contains("email"))
        assertTrue(exception.message!!.contains("name"))
        assertTrue(exception.message!!.contains("address"))
        assertTrue(exception.message!!.contains("zipCode"))
    }

    @DisplayName("정상 유저 생성")
    @Test
    fun createUser() {
        userService.createUser(getValidUser())
    }

    @DisplayName("유저 정보 업데이트")
    @Test
    fun updateUser() {
        val exception = assertThrows<ConstraintViolationException> {
            userService.updateUser(getValidUser())
        }
        assertTrue(exception.message!!.contains("user.id"))

        assertDoesNotThrow {
            userService.updateUser(getValidUser().apply { id = 1 })
        }

    }
}