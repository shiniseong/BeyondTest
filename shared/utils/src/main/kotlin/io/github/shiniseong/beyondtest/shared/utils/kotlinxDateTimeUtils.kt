package io.github.shiniseong.beyondtest.shared.utils

import kotlinx.datetime.*

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.plusDays(days: Long): LocalDateTime {
    val newDate = this.date.plus(days, DateTimeUnit.DAY)
    return LocalDateTime(newDate, this.time)
}

fun LocalDateTime.plusWeeks(weeks: Long): LocalDateTime {
    val newDate = this.date.plus(weeks, DateTimeUnit.WEEK)
    return LocalDateTime(newDate, this.time)
}

fun LocalDateTime.startOfDay(): LocalDateTime {
    return LocalDateTime(this.date, LocalTime(0, 0))
}

fun LocalDateTime.endOfDay(): LocalDateTime {
    return LocalDateTime(this.date, LocalTime(23, 59, 59, 999_999_999))
}