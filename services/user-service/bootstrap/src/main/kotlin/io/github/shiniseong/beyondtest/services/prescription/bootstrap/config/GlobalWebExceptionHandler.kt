package io.github.shiniseong.beyondtest.services.prescription.bootstrap.config

import io.github.shiniseong.beyondtest.shared.utils.now
import kotlinx.datetime.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val error: String,
    val message: String,
    val path: String,
    val type: String,
)

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(
        ex: ServerWebInputException,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<ErrorResponse>> {
        logger.error("예외 발생: ${ex.message}", ex)

        val message = ex.cause?.cause?.cause?.message ?: ex.message ?: "알 수 없는 요청 오류가 발생했습니다."
        val type = (ex.cause?.cause?.cause?.javaClass?.simpleName) ?: (ex::class.simpleName ?: "Unknown")
        val errorResponse = ErrorResponse(
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = message,
            path = exchange.request.uri.path,
            type = type
        )

        return Mono.just(ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST))
    }

    // 기타 예외를 처리하는 일반 핸들러
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, exchange: ServerWebExchange): Mono<ResponseEntity<ErrorResponse>> {
        logger.error("예외 발생: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = ex.message ?: "알 수 없는 내부 서버 오류가 발생했습니다.",
            path = exchange.request.uri.path,
            type = ex::class.simpleName ?: "Unknown"
        )

        return Mono.just(ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR))
    }
}