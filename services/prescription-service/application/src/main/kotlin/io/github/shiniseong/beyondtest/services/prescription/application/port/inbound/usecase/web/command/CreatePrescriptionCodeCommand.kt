package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.InvalidCreatePrescriptionCodeCommandException

data class CreatePrescriptionCodeCommand(
    val hospitalId: String,
) {
    init {
        require(hospitalId.isNotBlank()) { throw InvalidCreatePrescriptionCodeCommandException.default() }
    }
}
