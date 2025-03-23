package io.github.shiniseong.beyondtest.services.user.domain.enums

enum class OS {
    ANDROID,
    IOS,
    ;

    companion object {
        fun from(value: String): OS = valueOf(value.uppercase())
    }
}

typealias OsString = String

fun OsString.toOs(): OS = OS.from(this)