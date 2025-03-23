package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query.FindPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionCodeGrpcUseCase {
    suspend fun findAllPrescriptionCodeByUserIdAndStatus(query: FindPrescriptionCodeByUserIdAndStatusQuery): List<PrescriptionCode>
}