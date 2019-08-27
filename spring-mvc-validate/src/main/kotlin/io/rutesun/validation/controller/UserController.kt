package io.rutesun.validation.controller

import io.rutesun.validation.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user")
@Validated
class UserController {
    @PostMapping
    fun create(@Valid @RequestBody user: User, bindingResult: BindingResult): ResponseEntity<*> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.fieldError)
        }
        return ResponseEntity.ok(user)
    }
}