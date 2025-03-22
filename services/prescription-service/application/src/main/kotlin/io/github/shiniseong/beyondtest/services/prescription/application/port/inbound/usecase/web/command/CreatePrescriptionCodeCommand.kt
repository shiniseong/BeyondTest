package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.InvalidCreatePrescriptionCodeCommandException
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

data class CreatePrescriptionCodeCommand(
    val hospitalId: String,
) {
    init {
        require(hospitalId.isNotBlank()) { throw InvalidCreatePrescriptionCodeCommandException.default() }
    }

    fun toDomain(code: String) = PrescriptionCode.create(code = code, hospitalId = hospitalId)
}
