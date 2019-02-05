package io.rutesun.marshaller

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.reflect.full.createInstance

class MaskingSerializer(val masker: (String) -> String = { s -> s }) : StdSerializer<String>(String::class.java), ContextualSerializer {

    override fun serialize(value: String, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(masker(value))
    }

    override fun createContextual(provider: SerializerProvider, property: BeanProperty): JsonSerializer<*> {
        val ann = property.getAnnotation(MaskRequired::class.java)
        if (ann != null) {
            var masker = ann.masker.objectInstance ?: ann.masker.createInstance()
            return MaskingSerializer(masker)
        }
        return provider.findKeySerializer(property.type, property)
    }
}

