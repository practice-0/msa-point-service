

# Point Service

게시글 작성 시 포인트 차감 및 적립을 담당하는 내부 전용 서비스입니다.  
정합성이 중요한 도메인으로, 동기 호출 기반 처리 구조를 사용합니다.

---

## 1. 역할 (Responsibility)

- 게시글 작성 시 포인트 차감
- 활동 점수 적립
- 사용자별 포인트 상태 관리

외부에 직접 노출되지 않으며, 내부 서비스(Board Service)를 통해서만 호출됩니다.

---

## 2. API 구조

Internal API만 제공합니다.

### 포인트 적립

POST `/internal/points/add`

### 포인트 차감

POST `/internal/points/deduct`

---

## 3. 처리 흐름

### 게시글 작성 시 포인트 차감

1. Board Service에서 동기 호출
2. 사용자 포인트 조회
3. 차감 로직 수행
4. 트랜잭션 커밋

포인트 차감은 게시글 작성의 선행 조건이므로  
동기 처리 방식으로 설계했습니다.

---

## 4. 트랜잭션 처리

모든 포인트 변경 로직은 `@Transactional` 기반으로 처리됩니다.

- addPoints()
- deductPoints()

단일 인스턴스 환경을 가정한 기본 트랜잭션 처리 구조입니다.

---

## 5. 현재 설계 특징

- 단순 JPA 기반 상태 변경 구조
- 사용자별 단일 포인트 레코드 관리
- 포인트 변경은 동기 API 호출 기반 처리

현재 버전은 기본 동작에 집중한 구조이며,  
다음과 같은 보완이 가능합니다:

- Optimistic Lock(@Version) 기반 동시성 제어
- 포인트 부족 시 예외 처리 로직 강화
- 음수 방지 검증 로직 추가
- userId unique 제약 조건 명시

---

## 6. 내부 통신 원칙

- 서비스 간 DB 직접 접근 금지
- 포인트 변경은 반드시 Point Service를 통해 처리
- 비즈니스 로직은 서비스 레이어에 집중

---

## 7. 기술 스택

- Spring Boot
- Spring Data JPA
- MySQL (RDS)
- Internal ALB 기반 통신

---

## 8. Local 실행

```bash
docker-compose up -d
```

기본 포트: 8082