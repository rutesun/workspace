package com.rutesun.querydsl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class QueryDslApplication

fun main(args: Array<String>) {
    runApplication<QueryDslApplication>(*args)
}