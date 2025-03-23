package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment

import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response.AppEnvironmentResponse

interface AppEnvironmentGrpcClientPort {
    fun getLatestAppEnvironment(query: GetLatestAppEnvironmentQuery): AppEnvironmentResponse
}