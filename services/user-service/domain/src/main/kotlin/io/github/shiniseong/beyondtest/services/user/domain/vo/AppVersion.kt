package io.github.shiniseong.beyondtest.services.user.domain.vo

data class AppVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val preRelease: PreRelease? = null,
) : Comparable<AppVersion> {
    override fun compareTo(other: AppVersion): Int {
        // 주 버전 비교
        if (major != other.major) return major.compareTo(other.major)
        // 부 버전 비교
        if (minor != other.minor) return minor.compareTo(other.minor)
        // 패치 버전 비교
        if (patch != other.patch) return patch.compareTo(other.patch)
        // 프리 릴리즈 비교
        return when {
            preRelease == null && other.preRelease == null -> 0
            preRelease == null -> 1
            other.preRelease == null -> -1
            else -> preRelease.compareTo(other.preRelease)
        }
    }

    override fun toString(): String {
        return "$major.$minor.$patch${preRelease?.let { "-$it" } ?: ""}"
    }

    companion object {
        private val VERSION_REGEX = Regex(
            """^(\d+)\.(\d+)\.(\d+)(?:-([a-zA-Z]+)\.(\d+))?$"""
        )

        fun parse(versionString: String): AppVersion {
            val match = VERSION_REGEX.matchEntire(versionString)
                ?: throw IllegalArgumentException("유효하지 않은 version format 입니다.(version: $versionString)")

            val (major, minor, patch, preReleaseType, preReleaseVersion) = match.destructured

            val preRelease = if (preReleaseType.isNotBlank() && preReleaseVersion.isNotBlank()) {
                PreRelease(preReleaseType, preReleaseVersion.toInt())
            } else null

            return AppVersion(
                major.toInt(),
                minor.toInt(),
                patch.toInt(),
                preRelease
            )
        }
    }
}
