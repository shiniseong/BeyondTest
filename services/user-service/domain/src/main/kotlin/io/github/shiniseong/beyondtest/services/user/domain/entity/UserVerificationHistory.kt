package io.github.shiniseong.beyondtest.services.user.domain.entity

import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion
import io.github.shiniseong.beyondtest.shared.utils.now
import kotlinx.datetime.LocalDateTime

data class UserVerificationHistory(
    val id: Long,
    val userId: String,
    val version: AppVersion,
    val os: OS,
    val mode: BuildMode,
    val hash: String,
    val createdAt: LocalDateTime,
    val verified: Boolean,
    val message: String?
) {
    companion object {
        fun failure(
            message: String?,
            userId: String,
            version: AppVersion,
            os: OS,
            mode: BuildMode,
            hash: String,
        ) = UserVerificationHistory(
            id = 0,
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash,
            createdAt = LocalDateTime.now(),
            verified = false,
            message = message
        )

        fun success(
            hash: String,
            userId: String,
            version: AppVersion,
            os: OS,
            mode: BuildMode,
        ) = UserVerificationHistory(
            id = 0,
            userId = userId,
            version = version,
            os = os,
            mode = mode,
            hash = hash,
            createdAt = LocalDateTime.now(),
            verified = true,
            message = "검증 성공"
        )
    }
}
