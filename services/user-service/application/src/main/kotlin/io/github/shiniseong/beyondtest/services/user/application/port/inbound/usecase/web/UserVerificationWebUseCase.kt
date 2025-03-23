package io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.response.VerifyUserResponse

interface UserVerificationWebUseCase {
    fun verifyUser(command: VerifyUserCommand): VerifyUserResponse
}