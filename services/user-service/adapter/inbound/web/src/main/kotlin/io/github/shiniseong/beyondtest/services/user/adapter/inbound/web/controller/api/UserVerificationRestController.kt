package io.github.shiniseong.beyondtest.services.user.adapter.inbound.web.controller.api

import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.UserVerificationWebUseCase
import io.github.shiniseong.beyondtest.services.user.application.port.inbound.usecase.web.command.VerifyUserCommand
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class UserVerificationRestController(
    private val userVerificationService: UserVerificationWebUseCase
) : RouterFunction<ServerResponse> {
    private val delegate = coRouter {
        "/api/v1/users/verification".nest {
            POST("") { req ->
                val command = req.awaitBody<VerifyUserCommand>()
                ok().bodyValueAndAwait(userVerificationService.verifyUser(command))
            }
            GET("") { _ ->
                ok().bodyValueAndAwait(userVerificationService.findAll())
            }
        }
    }

    override fun route(request: ServerRequest): Mono<HandlerFunction<ServerResponse>> = delegate.route(request)
}