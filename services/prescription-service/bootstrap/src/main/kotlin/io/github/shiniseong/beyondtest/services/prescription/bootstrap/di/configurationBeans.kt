package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.config.serializer.JacksonCustomSerializeModule
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.scheduler.PrescriptionCodeScheduler
import org.springframework.context.support.beans

val configurationBeans = beans {
    bean<ObjectMapper>(isPrimary = true) {
        ObjectMapper().apply {
            findAndRegisterModules()
            registerModules(JacksonCustomSerializeModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
    bean {
        PrescriptionCodeScheduler(ref(), ref())
    }
}