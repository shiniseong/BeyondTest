package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query.GetPrescriptionCodeByUserIdAndStatusQuery
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionCodeGrpcUseCase {
    fun findPrescriptionCodeByUserIdAndStatus(query: GetPrescriptionCodeByUserIdAndStatusQuery): PrescriptionCode?
}