package io.github.shiniseong.beyondtest.services.user.domain.entity

import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import kotlinx.datetime.LocalDateTime

data class UserVerificationHistory(
    val userId: String,
    val version: AppVersion,
    val os: OS,
    val mode: BuildMode,
    val hash: String,
    val createdAt: LocalDateTime,
    val verified: Boolean,
    val message: String?
)
