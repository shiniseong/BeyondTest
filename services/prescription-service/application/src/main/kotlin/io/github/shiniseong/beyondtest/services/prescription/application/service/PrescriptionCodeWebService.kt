package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.PrescriptionCodeNotFoundException
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode

class PrescriptionCodeWebService(
    private val prescriptionCodeRepository: PrescriptionCodeRepositoryPort
) : PrescriptionCodeWebUseCase {
    override suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode {
        while (true) {
            val code = PrescriptionCode.generateCodeValue()
            if (prescriptionCodeRepository.findByCode(code) == null) {
                return prescriptionCodeRepository.insert(command.toDomain(code))
            }
        }
    }

    override suspend fun activatePrescriptionCode(command: ActivatePrescriptionCodeCommand): PrescriptionCode {
        val existingPrescriptionCode = prescriptionCodeRepository.findByCode(command.code)
            ?: throw PrescriptionCodeNotFoundException.default(command.code)

        return existingPrescriptionCode.activateFor(command.userId).also {
            prescriptionCodeRepository.update(it)
        }
    }

}