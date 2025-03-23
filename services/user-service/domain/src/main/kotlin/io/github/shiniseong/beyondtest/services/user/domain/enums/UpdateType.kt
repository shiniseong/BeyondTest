package io.github.shiniseong.beyondtest.services.user.domain.enums

/**
 * 업데이트 타입
 *
 * @property NONE 없음
 * @property RECOMMENDED 권장
 * @property REQUIRED 필수
 */
enum class UpdateType {
    /**
     * 없음
     */
    NONE,

    /**
     * 권장
     */
    RECOMMENDED,

    /**
     * 필수
     */
    REQUIRED,
    ;

    fun shouldUpdate(): Boolean = this != NONE
}