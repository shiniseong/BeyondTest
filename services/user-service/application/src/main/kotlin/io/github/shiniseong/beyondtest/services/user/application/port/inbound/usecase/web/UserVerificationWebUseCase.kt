package io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.response.VerifyUserResponse
import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory

interface UserVerificationWebUseCase {
    suspend fun verifyUser(command: VerifyUserCommand): VerifyUserResponse
    suspend fun findAll(): List<UserVerificationHistory>
}