package io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.response

import io.github.shiniseong.beyondtest.services.user.domain.enums.UpdateType

data class VerifyUserResponse(
    val updateType: UpdateType,
) {
    val shouldUpdate: Boolean
        get() = updateType.shouldUpdate()
}
