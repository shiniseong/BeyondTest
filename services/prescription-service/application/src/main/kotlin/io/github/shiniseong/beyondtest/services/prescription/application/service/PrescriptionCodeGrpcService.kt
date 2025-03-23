package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.PrescriptionCodeGrpcUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query.FindPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

class PrescriptionCodeGrpcService(
    private val prescriptionCodeRepository: PrescriptionCodeRepositoryPort
) : PrescriptionCodeGrpcUseCase {
    override suspend fun findAllPrescriptionCodeByUserIdAndStatus(query: FindPrescriptionCodeByUserIdAndStatusQuery): List<PrescriptionCode> =
        prescriptionCodeRepository.findAllByUserIdAndStatus(userId = query.userId, status = query.status)
}