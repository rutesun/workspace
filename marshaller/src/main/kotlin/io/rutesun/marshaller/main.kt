package io.rutesun.marshaller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.module.kotlin.KotlinModule

val objectMapper: ObjectMapper by lazy {
    val mapper = ObjectMapper()
    mapper.registerModule(KotlinModule())
    mapper.setAnnotationIntrospector(CustomAnnotationIntrospector())
    mapper
}

class CustomAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun findSerializer(a: Annotated): Any? {
        val ann = a.getAnnotation(MaskRequired::class.java)
        if (ann != null) {
            val serializer = ann.serializer.using
            return serializer.java
        }
        return super.findSerializer(a)
    }
}

object NameMasker : Masking {
    override operator fun invoke(s: String): String = Regex("(?<=.{1}).*").replace(s, "*")
}

data class User(
    @MaskRequired(masker = NameMasker::class)
    val name: String = ""
)

fun main(args: Array<String>) { }