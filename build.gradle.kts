plugins {
    // 코틀린 JVM 플러그인은 kotlin-jvm.gradle.kts 파일에서 적용된다.
    // 플러그인이 여러번 로드되는 것을 방지하기 필요한 플러그인은 최상위 레벨에서 apply false로 로드한다.
    // Kotlin plugins
    alias(libs.plugins.kotlinPluginSerialization) apply false
    // Spring plugins
    alias(libs.plugins.springKotlin) apply false
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.springDependencyManagement) apply false
    // ETC plugins
    alias(libs.plugins.protobuf) apply false
}