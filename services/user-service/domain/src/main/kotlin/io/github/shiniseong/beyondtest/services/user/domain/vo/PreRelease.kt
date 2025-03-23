package io.github.shiniseong.beyondtest.services.user.domain.vo

data class PreRelease(
    val type: String,
    val version: Int,
) : Comparable<PreRelease> {
    init {
        require(type.lowercase() in listOf("alpha", "beta")) { "유효하지 않은 pre-release type 입니다. (type: $type)" }
    }

    override fun compareTo(other: PreRelease): Int {
        if (type != other.type) {
            return if (type == "alpha") -1 else 1
        }

        return version.compareTo(other.version)
    }

    override fun toString(): String {
        return "$type.$version"
    }

    companion object {
        fun alpha(version: Int): PreRelease {
            return PreRelease("alpha", version)
        }

        fun beta(version: Int): PreRelease {
            return PreRelease("beta", version)
        }
    }
}
