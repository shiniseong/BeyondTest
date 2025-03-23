package io.github.shiniseong.beyondtest.services.prescription.application.port.inbound.usecase.web.command

import io.github.shiniseong.beyondtest.services.prescription.domain.vo.toPrescriptionCodeValue

data class ActivatePrescriptionCodeCommand(
    val userId: String,
    val code: String,
) {
    init {
        require(userId.isNotEmpty()) { "유저 ID가 공백으로 입력 되었습니다." }
        // 시간 관계상 일단 코드를 Value로 전환하면서 유효성 검증을 하고 있지만,
        // 암시적 검증(메소드 명에 드러나 있지 않음)이기 때문에 안티패턴임.
        // 차후 Command의 code의 데이터 타입 자체를 PrescriptionCodeValue로 변경 필요
        code.toPrescriptionCodeValue()
    }
}