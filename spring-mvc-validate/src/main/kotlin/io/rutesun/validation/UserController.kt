package io.rutesun.validation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @PostMapping
    fun create(user: User): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}