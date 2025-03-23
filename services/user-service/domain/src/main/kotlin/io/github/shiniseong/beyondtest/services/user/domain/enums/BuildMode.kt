package io.github.shiniseong.beyondtest.services.user.domain.enums

enum class BuildMode {
    DEBUG,
    RELEASE,
    ;

    companion object {
        fun fromValue(value: String): BuildMode {
            return valueOf(value.uppercase())
        }
    }
}

typealias BuildModeValue = String

fun BuildModeValue.toBuildMode(): BuildMode {
    return BuildMode.fromValue(this)
}