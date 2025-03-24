package io.github.shiniseong.beyondtest.services.prescription.bootstrap.transaction

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.domain.entity.PrescriptionCode
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

open class TransactionalPrescriptionCodeWebService(
    private val delegateService: PrescriptionCodeWebUseCase
) : PrescriptionCodeWebUseCase by delegateService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode =
        delegateService.createPrescriptionCode(command)

}