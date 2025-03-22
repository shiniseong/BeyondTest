package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.query

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.InvalidGetPrescriptionCodeByUserIdAndStatusQueryException
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

data class GetPrescriptionCodeByUserIdAndStatusQuery(
    val userId: String,
    val status: PrescriptionCodeStatus
) {
    init {
        require(userId.isNotBlank()) { throw InvalidGetPrescriptionCodeByUserIdAndStatusQueryException.default() }
    }
}
