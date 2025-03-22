package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

interface PrescriptionWebUseCase {
    fun createPrescription(command: CreatePrescriptionCodeCommand): PrescriptionCode
    fun activatePrescription(command: ActivatePrescriptionCodeCommand): PrescriptionCode
}