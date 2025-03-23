package io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc

import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.FindPrescriptionCodeByUserIdAndStatusRequest
import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.PrescriptionCodeServiceGrpc
import io.github.shiniseong.beyondtest.services.user.application.port.outbound.client.grpc.prescriptioncode.PrescriptionCodeClientPort
import org.slf4j.LoggerFactory

class PrescriptionCodeGrpcClient(
    private val prescriptionCodeServiceStub: PrescriptionCodeServiceGrpc.PrescriptionCodeServiceBlockingStub
) : PrescriptionCodeClientPort {
    private val logger = LoggerFactory.getLogger(PrescriptionCodeGrpcClient::class.java)
    override fun verifyPrescriptionCodeFor(userId: String): Boolean =
        runCatching {
            val request = FindPrescriptionCodeByUserIdAndStatusRequest.newBuilder().apply {
                setUserId(userId)
                setStatus("ACTIVATED")
            }.build()
            val activatedCode =
                prescriptionCodeServiceStub
                    .findAllByUserIdAndStatus(request)
                    .prescriptionCodesList
                    .firstOrNull()
            logger.info("Activated prescription code for user $userId is ${activatedCode?.code}")

            activatedCode != null
        }.onFailure {
            logger.error("Failed to verify prescription code for user $userId", it)
        }.getOrThrow()
}