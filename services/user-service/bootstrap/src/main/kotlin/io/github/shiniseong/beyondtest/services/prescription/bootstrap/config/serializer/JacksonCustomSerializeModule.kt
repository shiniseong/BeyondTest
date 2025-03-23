package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config.serializer

import com.fasterxml.jackson.databind.module.SimpleModule
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import kotlinx.datetime.LocalDateTime

class JacksonCustomSerializeModule : SimpleModule() {
    init {
        addSerializer(LocalDateTimeSerializer.Serializer())
        addDeserializer(LocalDateTime::class.java, LocalDateTimeSerializer.Deserializer())
        addSerializer(AppVersionSerializer.Serializer())
        addDeserializer(AppVersion::class.java, AppVersionSerializer.Deserializer())
    }
}