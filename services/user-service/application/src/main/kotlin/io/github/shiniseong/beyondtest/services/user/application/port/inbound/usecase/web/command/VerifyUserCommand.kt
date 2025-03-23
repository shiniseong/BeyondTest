package io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command

import io.github.shiniseong.beyondtest.services.user.domain.entity.UserVerificationHistory
import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion

data class VerifyUserCommand(
    val userId: String,
    val version: AppVersion,
    val os: OS,
    val mode: BuildMode,
    val hash: String,
) {
    init {
        require(userId.isNotBlank()) { "userId가 공백입니다." }
        require(hash.isNotBlank()) { "hash값이 공백입니다." }
    }

    fun toFailureHistory(message: String?) = UserVerificationHistory.failure(
        message = message,
        userId = userId,
        version = version,
        os = os,
        mode = mode,
        hash = hash,
    )

    fun toSuccessHistory() = UserVerificationHistory.success(
        hash = hash,
        userId = userId,
        version = version,
        os = os,
        mode = mode,
    )
}