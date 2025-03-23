package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.UserVerificationWebUseCase
import io.github.shiniseong.beyondtest.services.user.application.service.UserVerificationWebService
import org.springframework.context.support.beans


val applicationBeans = beans {
    bean<UserVerificationWebUseCase> {
        UserVerificationWebService(
            appEnvironmentClient = ref(),
            prescriptionCodeClient = ref(),
            userVerificationHistoryRepository = ref()
        )
    }
}