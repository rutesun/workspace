package io.rutesun.validation

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [UserController::class])
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = ObjectMapper()

    @Test
    fun createUser() {
        val user = User(name = "홍길동", email = "", birthday = LocalDate.now())
        val resultActions = mockMvc.perform(
            post("/user").content(mapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())

        println(resultActions.andReturn().response.contentAsString)
    }
}