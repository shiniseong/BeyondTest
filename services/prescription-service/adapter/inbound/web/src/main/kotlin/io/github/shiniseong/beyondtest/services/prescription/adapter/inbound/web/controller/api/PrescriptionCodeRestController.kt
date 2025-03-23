package io.github.shiniseong.beyondtest.services.prescription.adapter.inbound.web.controller.api

import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.PrescriptionCodeWebUseCase
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.ActivatePrescriptionCodeCommand
import io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command.CreatePrescriptionCodeCommand
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class PrescriptionCodeRestController(
    private val prescriptionCodeWebService: PrescriptionCodeWebUseCase
) : RouterFunction<ServerResponse> {
    private val delegate = coRouter {
        "/api/v1/prescription-codes".nest {
            POST("") { req ->
                val command = req.awaitBody<CreatePrescriptionCodeCommand>()
                ok().bodyValueAndAwait(prescriptionCodeWebService.createPrescriptionCode(command))
            }
            "/one/status/activated".nest {
                PATCH("") { req ->
                    val command = req.awaitBody<ActivatePrescriptionCodeCommand>()
                    ok().bodyValueAndAwait(prescriptionCodeWebService.activatePrescriptionCode(command))
                }
            }
        }
    }

    override fun route(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> = delegate.route(request)
}