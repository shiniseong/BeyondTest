package io.github.shiniseong.beyondtest.services.user.domain.vo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PreReleaseTest : StringSpec({
    "alpha와 beta중 beta가 더 크다" {
        // given
        val alpha = PreRelease.alpha(1)
        val beta = PreRelease.beta(2)

        // when
        val result = beta.compareTo(alpha)

        // then
        result shouldBe 1
    }

    "alpha와 beta중 alpha가 더 작다" {
        // given
        val alpha = PreRelease.alpha(1)
        val beta = PreRelease.beta(2)

        // when
        val result = alpha.compareTo(beta)

        // then
        result shouldBe -1
    }

    "pre-release가 같은 경우 version으로 비교한다" {
        // given
        val alpha1 = PreRelease.alpha(1)
        val alpha2 = PreRelease.alpha(2)

        // when
        val result = alpha1.compareTo(alpha2)

        // then
        result shouldBe -1
    }

    "alpha나 beta가 아닌 pre-release를 생성하려 하면 예외가 발생한다" {
        // when
        val exception = shouldThrow<IllegalArgumentException> {
            PreRelease("gamma", 1)
        }

        // then
        exception.message shouldBe "유효하지 않은 pre-release type 입니다. (type: gamma)"
    }
})