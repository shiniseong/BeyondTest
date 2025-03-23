package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

class LocalDateTimeSerializer {
    companion object {
        @OptIn(FormatStringsInDatetimeFormats::class)
        val formatter = LocalDateTime.Format { byUnicodePattern("yyyy-MM-dd'T'HH:mm:ss.SSS") }
    }

    class Serializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
        override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
            gen?.writeString(value?.format(formatter))
        }
    }

    class Deserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
            return LocalDateTime.parse(p?.text ?: "", formatter)
        }
    }
}