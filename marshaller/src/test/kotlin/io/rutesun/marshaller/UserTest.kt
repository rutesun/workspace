package io.rutesun.marshaller

import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {
    @Test
    fun `simple masking`() {
        val user = User("홍길동")
        val jsonStr = objectMapper.writeValueAsString(user)
        assertEquals("""
            {"name":"홍**"}
        """.trimIndent(), jsonStr)
    }
}