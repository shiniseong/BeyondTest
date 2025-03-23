package io.github.shiniseong.beyondtest.services.user.domain.vo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AppVersionTest : StringSpec({
    "앱 버전은 major.minor.patch-preRelease.version 으로 생성 될 수 있어야 한다." {
        // given
        val versionString = "1.2.3-alpha.4"

        // when
        val appVersion = versionString.toAppVersion()

        // then
        appVersion.major shouldBe 1
        appVersion.minor shouldBe 2
        appVersion.patch shouldBe 3
        appVersion.preRelease?.type shouldBe "alpha"
        appVersion.preRelease?.version shouldBe 4
    }

    "앱 버전은 major.minor.patch 만으로 생성될 수 있어야 한다." {
        // given
        val versionString = "1.2.3"

        // when
        val appVersion = versionString.toAppVersion()

        // then
        appVersion.major shouldBe 1
        appVersion.minor shouldBe 2
        appVersion.patch shouldBe 3
        appVersion.preRelease shouldBe null
    }

    "앱 버전 문자열이 올바르지 않으면 예외가 발생해야 한다." {
        // given
        val versionString = "1.2"

        // when
        val exception = shouldThrow<IllegalArgumentException> {
            versionString.toAppVersion()
        }
        // then
        exception.message shouldBe "유효하지 않은 version format 입니다.(version: 1.2)"
    }

    "두 버전이 같은지 판단할 수 있어야 한다." {
        // given
        val version1 = "1.2.3-alpha.4"
        val version2 = "1.2.3-alpha.4"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 == appVersion2) shouldBe true
    }

    "메이저 버전이 더 크면 뒤의 기타 버전 정보와 관계 없이 더 최신 버전으로 판단해야 한다." {
        // given
        val version1 = "2.0.0"
        val version2 = "1.10.10-beta.10"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }

    "메이저 버전이 같고 마이너 버전이 더 크면 뒤의 기타 버전 정보와 관계 없이 더 최신 버전으로 판단해야 한다." {
        // given
        val version1 = "1.3.0-alpha.4"
        val version2 = "1.2.10-beta.10"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }

    "메이저, 마이너 버전이 같고 패치 버전이 더 크면 뒤의 기타 버전 정보와 관계 없이 더 최신 버전으로 판단해야 한다." {
        // given
        val version1 = "1.2.3-alpha.4"
        val version2 = "1.2.2-beta.10"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }

    "메이저, 마이너, 패치 버전이 같고 preRelease 정보가 없으면 더 최신 버전으로 판단해야 한다." {
        // given
        val version1 = "1.2.3"
        val version2 = "1.2.3-beta.4"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }

    """메이저, 마이너, 패치 버전이 같고 preRelease 타입이 더 최신이면, 
        preRelease version이 더 크더라도 더 최신 버전으로 판단해야 한다.""" {
        // given
        val version1 = "1.2.3-beta.4"
        val version2 = "1.2.3-alpha.10"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }
    "메이저, 마이너, 패치 버전과 preRelease 타입이 같으면 preRelease version이 더 크면 더 최신 버전으로 판단해야 한다." {
        // given
        val version1 = "1.2.3-beta.10"
        val version2 = "1.2.3-beta.4"

        // when
        val appVersion1 = version1.toAppVersion()
        val appVersion2 = version2.toAppVersion()

        // then
        (appVersion1 > appVersion2) shouldBe true
    }
})