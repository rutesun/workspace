package io.rutesun.marshaller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.kotlin.KotlinModule

val objectMapper: ObjectMapper by lazy {
    val mapper = ObjectMapper()
    mapper.registerModule(KotlinModule())
    mapper
}

object NameMasker : Masking {
    override operator fun invoke(x: String): String = Regex("(?<=.{1}).*").replace(x, "*")
}

data class User(
    @JsonSerialize(using = MaskingSerializer::class)
    @MaskRequired(masker = NameMasker::class)
    val name: String = ""
)

fun main(args: Array<String>) { }