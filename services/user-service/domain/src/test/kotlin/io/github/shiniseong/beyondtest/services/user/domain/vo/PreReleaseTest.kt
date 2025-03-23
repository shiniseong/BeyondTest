package io.github.shiniseong.beyondtest.services.user.domain.vo

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
})