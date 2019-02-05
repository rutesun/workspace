package io.rutesun.marshaller

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KClass

interface Masking : (String) -> String {
    override fun invoke(s: String): String = s
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MaskRequired(
    val masker: KClass<out Masking>
)