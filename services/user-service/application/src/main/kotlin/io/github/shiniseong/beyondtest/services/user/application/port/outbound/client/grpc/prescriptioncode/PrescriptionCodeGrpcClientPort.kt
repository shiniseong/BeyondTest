package io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode

interface PrescriptionCodeGrpcClientPort {
    fun verifyAlreadyActivatedPrescriptionCode(userId: String): Boolean
}