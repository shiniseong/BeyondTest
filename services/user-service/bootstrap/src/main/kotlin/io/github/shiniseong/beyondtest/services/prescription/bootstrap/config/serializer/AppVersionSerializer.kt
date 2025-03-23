package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import io.github.shiniseong.beyondtest.services.user.domain.vo.toAppVersion

class AppVersionSerializer {
    class Serializer : StdSerializer<AppVersion>(AppVersion::class.java) {
        override fun serialize(value: AppVersion?, gen: JsonGenerator?, provider: SerializerProvider?) {
            gen?.writeString(value?.toString())
        }
    }

    class Deserializer : StdDeserializer<AppVersion>(AppVersion::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): AppVersion? {
            return p?.text?.toAppVersion()
        }
    }
}