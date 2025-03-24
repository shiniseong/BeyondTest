package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.grpc.PrescriptionCodeGrpcUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.system.PrescriptionCodeSystemUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeGrpcService
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeSystemService
import io.github.shiniseong.beyondtest.services.prescription.application.service.PrescriptionCodeWebService
import io.github.shiniseong.beyondtest.services.prescription.bootstrap.transaction.TransactionalPrescriptionCodeWebService
import org.springframework.context.support.beans

val applicationBeans = beans {
    bean {
        PrescriptionCodeWebService(ref())
    }
    bean<PrescriptionCodeWebUseCase>(isPrimary = true) {
        val delegate = ref<PrescriptionCodeWebService>()
        TransactionalPrescriptionCodeWebService(delegate)
    }
    bean<PrescriptionCodeGrpcUseCase> {
        PrescriptionCodeGrpcService(ref())
    }
    bean<PrescriptionCodeSystemUseCase> {
        PrescriptionCodeSystemService(ref())
    }
}