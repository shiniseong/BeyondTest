package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query

import io.github.shiniseong.beyondtest.services.user.domain.enums.BuildMode
import io.github.shiniseong.beyondtest.services.user.domain.enums.OS

data class GetLatestAppEnvironmentQuery(
    val os: OS,
    val mode: BuildMode,
)
