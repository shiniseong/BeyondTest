package io.github.shiniseong.beyondtest.shared.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class KotlinxDateTimeUtilsTest : StringSpec({
    "plusDays(1)는 하루를 더한 날짜를 반환해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.plusDays(1)

        result.date shouldBe LocalDate(2024, 3, 24)
        result.time shouldBe LocalTime(10, 30)
    }

    "plusDays(5)는 5일을 더한 날짜를 반환해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.plusDays(5)

        result.date shouldBe LocalDate(2024, 3, 28)
        result.time shouldBe LocalTime(10, 30)
    }

    "plusDays()는 월을 넘어가는 일수를 더할 수 있어야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 30, 10, 30)
        val result = dateTime.plusDays(3)

        result.date shouldBe LocalDate(2024, 4, 2)
        result.time shouldBe LocalTime(10, 30)
    }

    "plusWeeks(1)는 1주일을 더한 날짜를 반환해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.plusWeeks(1)

        result.date shouldBe LocalDate(2024, 3, 30)
        result.time shouldBe LocalTime(10, 30)
    }

    "plusWeeks(2)는 2주일을 더한 날짜를 반환해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.plusWeeks(2)

        result.date shouldBe LocalDate(2024, 4, 6)
        result.time shouldBe LocalTime(10, 30)
    }

    "startOfDay()는 날짜의 시작 시간(00:00)으로 설정해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.startOfDay()

        result.date shouldBe LocalDate(2024, 3, 23)
        result.time shouldBe LocalTime(0, 0)
    }

    "endOfDay()는 날짜의 끝 시간(23:59:59.999999999)으로 설정해야 합니다." {
        val dateTime = LocalDateTime(2024, 3, 23, 10, 30)
        val result = dateTime.endOfDay()

        result.date shouldBe LocalDate(2024, 3, 23)
        result.time shouldBe LocalTime(23, 59, 59, 999_999_999)
    }
})