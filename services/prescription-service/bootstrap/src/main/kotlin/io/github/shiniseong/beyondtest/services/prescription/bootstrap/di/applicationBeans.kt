package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeWebService
import org.springframework.context.support.beans

val applicationBeans = beans {
    bean<PrescriptionCodeWebUseCase> {
        PrescriptionCodeWebService(ref())
    }
}