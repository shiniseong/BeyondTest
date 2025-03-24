package io.github.shiniseong.beyondtest.shared.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.util.concurrent.atomic.AtomicInteger

class RetryScopeTest : StringSpec({
    "첫 번째 시도에서 성공하면 그 결과를 반환해야 한다" {
        // given
        val expected = "성공"

        // when
        val result = retry {
            expected
        }

        // then
        result.isSuccess shouldBe true
        result.getOrNull() shouldBe expected
    }

    "지정된 횟수만큼 재시도한 후 성공하면 그 결과를 반환해야 한다" {
        // given
        val expected = "성공"
        val failCount = AtomicInteger(2) // 처음 2번은 실패, 3번째에 성공

        // when
        val result = retry(maxAttempts = 3) {
            if (failCount.getAndDecrement() > 0) {
                throw RuntimeException("의도적인 실패")
            }
            expected
        }

        // then
        result.isSuccess shouldBe true
        result.getOrNull() shouldBe expected
    }

    "최대 재시도 횟수를 초과하면 실패 결과를 반환해야 한다" {
        // given
        val maxAttempts = 3
        val exception = RuntimeException("의도적인 실패")

        // when
        val result = retry(maxAttempts = maxAttempts) {
            throw exception
        }

        // then
        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe exception
    }

    "재시도할 때마다 onRetry 콜백이 호출되어야 한다" {
        // given
        val maxAttempts = 3
        val onRetry = mockk<(Throwable, Attempt) -> Unit>(relaxed = true)
        val exception = RuntimeException("의도적인 실패")

        // when
        retry(
            maxAttempts = maxAttempts,
            onRetry = onRetry
        ) {
            throw exception
        }

        // then
        // onRetry가 maxAttempts 횟수만큼 호출되었는지 검증
        verify(exactly = maxAttempts) { onRetry(any(), any()) }
        // 첫 번째 시도 후 호출됨
        verify { onRetry(exception, 1) }
        // 두 번째 시도 후 호출됨
        verify { onRetry(exception, 2) }
        // 세 번째 시도 후 호출됨
        verify { onRetry(exception, 3) }
    }

    "최대 재시도 횟수에 도달하면 onMaxAttemptsReached 콜백이 호출되어야 한다" {
        // given
        val maxAttempts = 3
        val onMaxAttemptsReached = mockk<(Attempt) -> Unit>(relaxed = true)

        // when
        retry(
            maxAttempts = maxAttempts,
            onMaxAttemptsReached = onMaxAttemptsReached
        ) {
            throw RuntimeException("의도적인 실패")
        }

        // then
        verify(exactly = 1) { onMaxAttemptsReached(maxAttempts) }
    }

    "시간 간격을 두고 재시도되어야 한다" {
        // given
        val interval = 100L // 빠른 테스트를 위해 짧은 간격 사용
        val startTime = System.currentTimeMillis()
        val tries = 3

        // when
        retry(
            maxAttempts = tries,
            interval = interval
        ) {
            throw RuntimeException("의도적인 실패")
        }

        // then
        val duration = System.currentTimeMillis() - startTime
        // 최소한 (tries-1) * interval 시간이 경과해야 함
        // 첫 번째 시도는 간격 없이 즉시 실행되므로 (tries-1)
        (duration >= (tries - 1) * interval) shouldBe true
    }

    "실행 중 다른 유형의 예외가 발생해도 재시도해야 한다" {
        // given
        val attemptCounter = AtomicInteger(0)

        // when
        val result = retry(maxAttempts = 3) {
            val attempt = attemptCounter.incrementAndGet()
            when (attempt) {
                1 -> throw IllegalArgumentException("첫 번째 예외")
                2 -> throw IllegalStateException("두 번째 예외")
                else -> "성공"
            }
        }

        // then
        result.isSuccess shouldBe true
        result.getOrNull() shouldBe "성공"
        attemptCounter.get() shouldBe 3
    }

    "커스텀 실패 조건으로 특정 예외만 재시도할 수 있다" {
        // given
        class NonRetryableException : Exception("재시도하지 않는 예외")

        var attemptCounter = 0

        // when & then
        val result = retry(
            maxAttempts = 3,
            onRetry = { e, attempt ->
                // NonRetryableException이면 재시도하지 않고 다시 throw
                attemptCounter = attempt
                if (e is NonRetryableException) {
                    throw e
                }
            }
        ) {
            throw NonRetryableException()
        }


        result.isSuccess shouldBe false
        attemptCounter shouldBe 1
        result.exceptionOrNull()?.message shouldBe "재시도하지 않는 예외"
    }
})