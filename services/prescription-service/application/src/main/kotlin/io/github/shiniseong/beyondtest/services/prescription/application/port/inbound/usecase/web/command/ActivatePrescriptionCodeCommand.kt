package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command

data class ActivatePrescriptionCodeCommand(
    val userId: String,
    val code: String,
)