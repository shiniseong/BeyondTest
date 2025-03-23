package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionCodeWebUseCase {
    suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode
    suspend fun activatePrescriptionCode(command: ActivatePrescriptionCodeCommand): PrescriptionCode
}