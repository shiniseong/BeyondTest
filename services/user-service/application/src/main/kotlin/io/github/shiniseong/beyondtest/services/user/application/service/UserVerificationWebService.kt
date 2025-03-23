package io.github.shiniseong.beyondtest.services.user.application.service

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.UserVerificationWebUseCase
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.response.VerifyUserResponse
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.AppEnvironmentClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.query.GetLatestAppEnvironmentQuery
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.appenvironment.response.AppEnvironmentResponse
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode.PrescriptionCodeClientPort
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.repository.UserVerificationHistoryRepositoryPort
import io.github.shiniseong.beyondtest.services.user.domain.enums.UpdateType
import io.github.shiniseong.beyondtest.services.user.domain.vo.AppVersion

class UserVerificationWebService(
    private val appEnvironmentClient: AppEnvironmentClientPort,
    private val prescriptionCodeClient: PrescriptionCodeClientPort,
    private val userVerificationHistoryRepository: UserVerificationHistoryRepositoryPort
) : UserVerificationWebUseCase {
    override suspend fun verifyUser(command: VerifyUserCommand): VerifyUserResponse {
        val latestAppEnvironment = appEnvironmentClient.getLatestAppEnvironment(
            GetLatestAppEnvironmentQuery(command.os, command.mode)
        )
        runCatching {
            require(command.hash in latestAppEnvironment.validHashes) { "App 검증에 실패했습니다. Hash값이 유효하지 않습니다." }
            require(command.userId.hasActivatedPrescriptionCode()) { "활성화 된 처방 코드가 없습니다." }
        }
            .onSuccess {
                userVerificationHistoryRepository.insert(command.toSuccessHistory())
            }
            .onFailure {
                userVerificationHistoryRepository.insert(command.toFailureHistory(it.message))
                throw it
            }

        return VerifyUserResponse(
            calculateUpdateType(latestAppEnvironment, command.version),
        )
    }

    private fun calculateUpdateType(
        environmentResponse: AppEnvironmentResponse,
        userAppVersion: AppVersion
    ): UpdateType =
        if (userAppVersion < environmentResponse.minimumRequiredVersion) {
            UpdateType.REQUIRED
        } else if (userAppVersion < environmentResponse.latestVersion) {
            UpdateType.RECOMMENDED
        } else {
            UpdateType.NONE
        }

    private fun String.hasActivatedPrescriptionCode(): Boolean = prescriptionCodeClient.verifyPrescriptionCodeFor(this)
}

