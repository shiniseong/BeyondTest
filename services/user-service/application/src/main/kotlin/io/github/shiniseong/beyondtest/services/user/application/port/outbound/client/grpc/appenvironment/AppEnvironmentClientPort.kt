package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment

import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response.AppEnvironmentResponse

interface AppEnvironmentClientPort {
    fun getLatestAppEnvironment(query: GetLatestAppEnvironmentQuery): AppEnvironmentResponse
}