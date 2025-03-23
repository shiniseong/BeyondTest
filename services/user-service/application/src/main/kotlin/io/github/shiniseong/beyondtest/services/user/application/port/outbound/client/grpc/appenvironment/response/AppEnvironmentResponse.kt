package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response

import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion

data class AppEnvironmentResponse(
    val latestVersion: AppVersion,
    val minimumRequiredVersion: AppVersion,
    val validHash: String,
)
