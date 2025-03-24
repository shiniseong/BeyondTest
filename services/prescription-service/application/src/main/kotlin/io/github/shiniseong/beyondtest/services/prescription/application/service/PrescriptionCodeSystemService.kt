package io.github.shiniseong.beyondtest.services.prescription.application.service

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.system.PrescriptionCodeSystemUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.outbound.repository.PrescriptionCodeRepositoryPort
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import io.github.shiniseong.beyondtest.shared.utils.now
import kotlinx.datetime.LocalDateTime

class PrescriptionCodeSystemService(
    private val prescriptionCodeRepository: PrescriptionCodeRepositoryPort,
) : PrescriptionCodeSystemUseCase {
    override suspend fun expirePrescriptionCode(): List<PrescriptionCode> {
        val now = LocalDateTime.now()
        val expiredPrescriptionCodes = prescriptionCodeRepository.findAllToExpireFrom(now).map { it.expired() }
        return prescriptionCodeRepository.updateAll(expiredPrescriptionCodes)
    }
}