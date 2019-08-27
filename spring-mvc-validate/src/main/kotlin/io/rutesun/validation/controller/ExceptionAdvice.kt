package io.rutesun.validation.controller

import io.rutesun.validation.domain.ValidationErrors
import io.rutesun.validation.domain.Violation
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ExceptionAdvice {
    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onConstraintValidationException(e: ConstraintViolationException): ResponseEntity<ValidationErrors> {
        val error = ValidationErrors()
        for (violation in e.constraintViolations) {
            error.add(Violation(violation.propertyPath.toString(), violation.message))
        }
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrors> {
        val error = ValidationErrors()
        for (fieldError in e.bindingResult.fieldErrors) {
            error.add(Violation(fieldError.field, fieldError.defaultMessage))
        }
        return ResponseEntity.badRequest().body(error)
    }
}

