# 비욘드메디슨 채용과제

이 프로젝트는 비욘드메디슨 채용 과제로, 헥사고날 아키텍처와 MSA(Microservice Architecture)를 기반으로 처방코드 관리 및 사용자 검증 서비스를 구현한 애플리케이션입니다.

[API 명세(Swagger)](http://15.164.164.149:8081/swagger-ui.html)

[API 명세(포스트맨)](https://documenter.getpostman.com/view/43352676/2sAYkHoy9x)

## 프로젝트 목표 및 개요

- 처방코드 생성, 활성화, 사용자 검증 기능을 구현
- 헥사고날 아키텍처 적용 (과제의 규모가 크고 복잡하지는 않지만, 아키텍처 설계 연습을 위해 적용)
- MSA를 최대한 간소화 시켜 모방
- 현재 비욘드 메디슨 에서 사용 중인 기술 스택 연습을 위해 서비스 간 통신을 위한 gRPC 구현
- 도메인 주도 설계 원칙을 따르는 풍부한 도메인 모델 지향
- 외부 세계와 멀리 떨어진 하부의 순수 함수들 부터 단위 테스트를 진행 (테스트 주도 개발)
- Kotlin 언어 기능(infix fun, 확장 함수 등)을 활용한 가독성 좋은 코드 작성

## 기술 스택

- **언어**: Kotlin
- **프레임워크**: Spring Boot, Spring WebFlux
- **데이터베이스**: H2 (인메모리)
- **DB 접근 기술**: Spring Data R2DBC
- **빌드 도구**: Gradle (멀티 모듈)
- **서비스 간 통신**: gRPC
- **비동기 처리**: Coroutines, Reactor
- **테스트**: Kotest, MockK

## 서비스 구조

프로젝트는 아래 서비스로 구성되어 있습니다.

1. Prescription Service: 처방코드 생성 및 활성화 관리
2. User Service: 사용자 검증
3. ~~App Environment Service (시간 관계상 미구현)~~
4. ~~Notification Service (시간 관계상 미구현)~~

## 아키텍처

헥사고날 아키텍처를 기반으로 각 서비스는 다음 계층으로 구분됩니다.

1. **도메인 계층 (Domain Layer)**

- 비즈니스 엔티티, 값 객체 (Value Objects)
- 도메인 규칙 및 비즈니스 로직
- 도메인 이벤트 및 예외

2. **애플리케이션 계층 (Application Layer)**

- 유스케이스 구현
- 포트 정의 (인바운드/아웃바운드)

3. **어댑터 계층 (Adapter Layer)**

- **인바운드 어댑터**: 외부에서 애플리케이션으로의 요청 처리 (Web 컨트롤러, gRPC 서비스)
- **아웃바운드 어댑터**: 애플리케이션에서 외부로의 요청 처리 (데이터베이스, 외부 서비스 클라이언트)

4. **부트스트랩 계층 (Bootstrap Layer)**

- 애플리케이션 진입점 (메인 클래스)
- 의존성 주입 및 빈 구성
- 환경 설정 및 프로퍼티 관리

## 구현 기능

### 1. 처방코드 생성 API (Prescription Service)

- 대문자와 숫자를 조합한 8자리 랜덤 코드 생성
- 중복되지 않는 코드 보장
- 생성 이력 관리

### 2. 처방코드 활성화 API (Prescription Service)

- 기존 처방코드가 만료되었거나 없을 때만 활성화 가능
- 활성화 후 6주 뒤 자정에 만료
- 스케줄러를 통한 자동 만료 처리

### 3. 사용자 검증 API (User Service)

- 사용자 버전, OS, 빌드 모드, 해시값 검증
- 버전에 따른 업데이트 권장/강제 정보 제공
- 유효한 처방코드 보유 여부 확인
- 검증 이력 관리

## 아쉬운 점

구현 과정에서 파악된 개선이 필요한 부분들입니다. 시간 관계상 미처 개선 하지 못한 부분들에 대해 기술합니다.

1. **패키지 구조 개선**
    - `repository.repoimpl`의 네이밍이 직관적이지 않음. `persistence.repository`로 변경하면 더 명확할 것 같습니다.
    - 기술 의존적인 패키지 네이밍(`grpc`, `web` 등) 대신 역할 기반 네이밍을 사용하는 것이 좋을 것 같습니다.
        - `module`: 다른 모듈에서 호출하는 컴포넌트
        - `actor`: 사용자에 의해 직접 호출되는 컴포넌트
        - `system`: 배치 작업이나 이벤트에 의해 호출되는 컴포넌트

2. **만료 처리 개선**
    - 만료 예정일(`expectedExpiredAt`)과 실제 만료일(`actualExpiredAt`)을 구분해서 관리하면 비정상 만료된 건수를 추적하고 분석하는 데 도움이 될 것입니다.

3. **예외 처리 개선**
    - 현재 예외들이 충분히 구조화되어 있지 않습니다. 예외 계층을 더 체계적으로 구성하고, `GlobalErrorHandler`에서 더 상세하게 처리할 필요가 있습니다.

4. **gRPC 구현 개선**
    - gRPC를 처음 사용하면서 Proto 파일 공유의 중요성을 간과했습니다. 서비스 간에 Proto 파일을 공유하면 유지 보수가 훨씬 쉬워집니다.

5. **여러 서비스간 분산 트랜잭션 적용**
    - 서비스별로 별도의 데이터베이스를 운용하는 구조이기 때문에 특정 서비스에서 예외 발생시 관련 서비스가 롤백 할 수 있는 기능 구현이 필요해 보입니다.
6. **벌크 업데이트 기능 부재**
   - 6주 후 만료 배치 작업시 여러 건의 업데이트를 각각 순회하며 진행하도록 되어 있습니다. 한번의 커넥션으로 벌크 업데이트가 가능하도록 수정이 필요합니다.

## 동시성 이슈 (해결. 하단의 해결 된 부분 참조)

프로젝트 구현 중 발견된 동시성 문제가 발생할 수 있는 부분입니다.

### 처방코드 생성 시 동시성 문제

```kotlin
override suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode {
    while (true) {
        val code = PrescriptionCode.generateCodeValue()
        if (prescriptionCodeRepository.findByCode(code) == null) {
            // 이 시점에서 다른 요청이 같은 코드를 생성하고 삽입할 수 있음(Race Condition)
            return prescriptionCodeRepository.insert(command.toDomain(code))
        }
    }
}
```

위 코드는 동시에 여러 요청이 들어올 경우 동일한 코드를 중복 생성할 가능성이 있습니다.

1. 데이터베이스 수준에서 유니크 제약 조건 설정(현재 구현됨)
2. 분산 락(Distributed Lock) 사용
3. DB 트랜잭션 격리 수준 조정(현재 구현됨)
4. 낙관적 락(Optimistic Locking) 구현

## 해결된 문제

### 처방코드 생성 시 동시성 문제 해결

앞서 언급한 처방코드 생성 시 발생할 수 있는 동시성 문제를 다음과 같이 해결했습니다.
[관련 내용 블로그 포스팅](https://shin-e-dog.tistory.com/150)

#### 데코레이터 패턴과 SERIALIZABLE 트랜잭션 격리 수준 적용

기존 서비스 로직을 수정하지 않고 트랜잭션 처리를 위한 데코레이터를 추가하여 동시성 문제를 해결했습니다.

```kotlin
open class TransactionalPrescriptionCodeWebService(
    private val delegateService: PrescriptionCodeWebUseCase
) : PrescriptionCodeWebUseCase by delegateService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override suspend fun createPrescriptionCode(command: CreatePrescriptionCodeCommand): PrescriptionCode =
        delegateService.createPrescriptionCode(command)
}
```

`SERIALIZABLE` 격리 수준은 팬텀 삽입 문제를 방지하여, 동일한 코드에 대한 중복 생성 시도를 데이터베이스 수준에서 차단합니다. 트랜잭션이 범위 락(Range Lock)을 설정하기 때문에, 한 트랜잭션이
특정 코드가 존재하지 않음을 확인한 후 다른 트랜잭션이 동일한 코드를 삽입하는 것을 방지합니다.
Read 작업과 같이 빈번하게 발생하는 작업이 아니기 때문에 성능에 큰 영향을 미치지 않을 것으로 판단했습니다.

#### DI 설정

부트스트랩 계층의 빈 설정 파일에서 데코레이터 패턴을 적용했습니다:

```kotlin
// bootstrap/di/applicationBeans.kt
val applicationBeans = beans {
    // 원본 서비스 빈 등록
    bean {
        PrescriptionCodeWebService(ref())
    }

    // 데코레이터를 주 빈(primary)으로 등록하여 주입 시 이 빈이 사용되도록 함
    bean<PrescriptionCodeWebUseCase>(isPrimary = true) {
        val delegate = ref<PrescriptionCodeWebService>()
        TransactionalPrescriptionCodeWebService(delegate)
    }
}
```

이 방식은 기존 서비스 로직을 수정하지 않고도 트랜잭션 관리를 할 수 있고, 도메인 계층이 특정 기술에 종속되지 않도록 하는 데 도움이 됩니다.

## 프로젝트 실행 방법

### 요구사항

- JDK 17 이상
- Gradle 8.0 이상

### 실행 단계

1. 저장소 클론

```
https://github.com/shiniseong/BeyondTest.git
```

3. 빌드

```bash
./gradlew build
```

3. 각 서비스 실행

```bash
# Prescription Service 실행 (8081 포트)
./gradlew :services:prescription-service:bootstrap:bootRun

# User Service 실행 (8082 포트)
./gradlew :services:user-service:bootstrap:bootRun
```

## 프로젝트 구조

이 프로젝트는 멀티 모듈 구조로 각 서비스는 도메인, 애플리케이션, 어댑터, 부트스트랩 모듈로 분리되어 있습니다.

```
beyondtest/
├── shared/
│   └── utils/                 # 공통 유틸리티 모듈
├── services/
│   ├── prescription-service/  # 처방코드 서비스
│   │   ├── domain/           # 도메인 모델
│   │   ├── application/      # 유스케이스
│   │   ├── adapter/          # 어댑터
│   │   │   ├── inbound/      # 인바운드 어댑터(웹 Controller, gRPC Service 등)
│   │   │   └── outbound/     # 아웃바운드 어댑터(저장소, gRPC 클라이언트 등)
│   │   └── bootstrap/        # 애플리케이션 설정 및 실행
│   └── user-service/         # 사용자 서비스
│       ├── domain/
│       ├── application/
│       ├── adapter/
│       │   ├── inbound/
│       │   └── outbound/
│       └── bootstrap/
```

## 테스트

각 계층별로 단위 테스트와 통합 테스트가 구현되어 있습니다.

- 도메인 로직 테스트
- 유스케이스 및 서비스 테스트
- 웹 및 gRPC 컨트롤러 테스트
- 저장소 및 클라이언트 테스트

테스트 실행:

```bash
./gradlew test
```
