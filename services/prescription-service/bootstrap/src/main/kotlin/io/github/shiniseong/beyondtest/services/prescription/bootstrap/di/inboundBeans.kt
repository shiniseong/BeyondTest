package io.github.shiniseong.beyondtest.services.prescription.bootstrap.di

import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.PrescriptionCodeGrpcServiceAdapter
import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.web.controller.api.PrescriptionCodeRestController
import org.springframework.context.support.beans

val controllerBeans = beans {
    bean { PrescriptionCodeRestController(ref()) }
}
val grpcBeans = beans {
    bean { PrescriptionCodeGrpcServiceAdapter(ref()) }
}