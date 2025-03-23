package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.user.adapter.inbound.web.controller.api.UserVerificationRestController
import org.springframework.context.support.beans

val controllerBeans = beans {
    bean { UserVerificationRestController(ref()) }
}
