package io.github.shiniseong.beyondtest.services.user.adapter.outbound.grpc

import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.FindPrescriptionCodeByUserIdAndStatusRequest
import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.FindPrescriptionCodeByUserIdAndStatusResponse
import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.PrescriptionCodeProto
import io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.grpc.PrescriptionCodeServiceGrpc
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class PrescriptionCodeGrpcClientTest : StringSpec({
    // Mock the gRPC stub
    val mockStub = mockk<PrescriptionCodeServiceGrpc.PrescriptionCodeServiceBlockingStub>()

    // Create the client to test
    val client = PrescriptionCodeGrpcClient(mockStub)

    beforeTest {
        clearAllMocks()
    }

    "사용자에게 활성화된 처방 코드가 있을 경우 verifyPrescriptionCodeFor는 true를 반환해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val expectedRequest = FindPrescriptionCodeByUserIdAndStatusRequest.newBuilder()
            .setUserId(userId)
            .setStatus("ACTIVATED")
            .build()

        val prescriptionCode = PrescriptionCodeProto.newBuilder()
            .setCode("Y6U8Y8U8")
            .setStatus("ACTIVATED")
            .setCreatedBy("hospitalId123")
            .setCreatedAt("2025-03-24T10:00:00")
            .setActivatedFor(userId)
            .setActivatedAt("2025-03-24T10:00:00")
            .build()

        val response = FindPrescriptionCodeByUserIdAndStatusResponse.newBuilder()
            .addPrescriptionCodes(prescriptionCode)
            .build()

        every {
            mockStub.findAllByUserIdAndStatus(match {
                it.userId == expectedRequest.userId && it.status == expectedRequest.status
            })
        } returns response

        // when
        val result = client.verifyPrescriptionCodeFor(userId)

        // then
        result shouldBe true
        verify(exactly = 1) { mockStub.findAllByUserIdAndStatus(any()) }
    }

    "사용자에게 활성화된 처방 코드가 없을 경우 verifyPrescriptionCodeFor는 false를 반환해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val expectedRequest = FindPrescriptionCodeByUserIdAndStatusRequest.newBuilder()
            .setUserId(userId)
            .setStatus("ACTIVATED")
            .build()

        val emptyResponse = FindPrescriptionCodeByUserIdAndStatusResponse.newBuilder()
            .build()

        every {
            mockStub.findAllByUserIdAndStatus(match {
                it.userId == expectedRequest.userId && it.status == expectedRequest.status
            })
        } returns emptyResponse

        // when
        val result = client.verifyPrescriptionCodeFor(userId)

        // then
        result shouldBe false
        verify(exactly = 1) { mockStub.findAllByUserIdAndStatus(any()) }
    }

    "gRPC 호출에서 예외가 발생하면 verifyPrescriptionCodeFor는 해당 예외를 전파해야 한다" {
        // given
        val userId = "e4e3ecbd-2208-4905-8120-426473d0eae9"
        val expectedRequest = FindPrescriptionCodeByUserIdAndStatusRequest.newBuilder()
            .setUserId(userId)
            .setStatus("ACTIVATED")
            .build()

        val exception = RuntimeException("gRPC connection failed")
        every { mockStub.findAllByUserIdAndStatus(any()) } throws exception

        // when/then
        val thrownException = shouldThrow<RuntimeException> {
            client.verifyPrescriptionCodeFor(userId)
        }

        // then
        thrownException.message shouldBe "gRPC connection failed"
        verify(exactly = 1) { mockStub.findAllByUserIdAndStatus(any()) }
    }
})