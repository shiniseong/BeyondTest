package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command

import io.github.shiniseong.beyondtest.services.prescription.domain.vo.PrescriptionCodeValue

data class ActivatePrescriptionCodeCommand(
    val userId: String,
    val prescriptionCodeValue: PrescriptionCodeValue,
)
