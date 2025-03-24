package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.AlreadyExistActivatedPrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.exception.PrescriptionCodeNotFoundException
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.services.prescription.domain.enums.PrescriptionCodeStatus

class PrescriptionCodeWebService(
    private val prescriptionCodeRepository: PrescriptionCodeRepositoryPort
) : PrescriptionCodeWebUseCase {
    override suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode {
        while (true) {
            val code = PrescriptionCode.generateCodeValue()
            if (prescriptionCodeRepository.findByCode(code) == null) {
                //TODO 동시에 같은 코드가 생성되는 경우를 방지하기 위해 동시성 제어가 필요함
                return prescriptionCodeRepository.insert(command.toDomain(code))
            }
        }
    }

    override suspend fun activatePrescriptionCode(command: ActivatePrescriptionCodeCommand): PrescriptionCode {
        val existingPrescriptionCode = prescriptionCodeRepository.findByCode(command.code)
            ?: throw PrescriptionCodeNotFoundException.default(command.code)

        require(existingPrescriptionCode.status.isCreated()) { "이미 활성화 되었거나 만료된 코드입니다." }

        prescriptionCodeRepository
            .findAllByUserIdAndStatus(command.userId, PrescriptionCodeStatus.ACTIVATED)
            .firstOrNull()
            ?.let { alreadyActivatedCode ->
                throw AlreadyExistActivatedPrescriptionCode(alreadyActivatedCode.code.value)
            }
        val activatedCode = existingPrescriptionCode.activateFor(command.userId)
        return prescriptionCodeRepository.update(activatedCode)
    }

    override suspend fun findAll(): List<PrescriptionCode> {
        return prescriptionCodeRepository.findAll()
    }

}