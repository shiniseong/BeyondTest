package io.github.shiniseong.beyondtest.shared.utils

typealias Attempt = Int

fun <T> retry(
    maxAttempts: Attempt = 3,
    interval: Long = 500L,
    onRetry: (Throwable, Attempt) -> Unit = { _, _ -> },
    onMaxAttemptsReached: (Attempt) -> Unit = {},
    operation: () -> T
): Result<T> {
    var attempts = 0
    while (attempts < maxAttempts) {
        try {
            try {
                return Result.success(operation())
            } catch (e: Throwable) {
                attempts++
                onRetry(e, attempts)
                if (attempts >= maxAttempts) {
                    onMaxAttemptsReached(attempts)
                    return Result.failure(e)
                }
                Thread.sleep(interval)
            }
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }
    return Result.failure(IllegalStateException("unreachable"))
}