Spring 3.5.10
Java 17


# 변경될 구조
LXP-BE/                           # 단일 Gradle 프로젝트
├─ build.gradle                   # 전체 빌드 설정 (하나만)
├─ settings.gradle
├─ gradlew
├─ .gradle/
│
└─ src/main/java/com/lxp/        # 통합 패키지 루트
├─ LxpApplication.java       # @SpringBootApplication (메인)
│
├─ common/                    # 공통 모듈
│  ├─ exception/
│  ├─ config/
│  └─ util/
│
├─ auth/                      # Auth 도메인 (패키지)
│  ├─ domain/
│  ├─ application/
│  └─ infrastructure/
│
├─ user/                      # User 도메인
├─ member/                    # Member 도메인
├─ course/                    # Course 도메인
├─ enrollment/                # Enrollment 도메인
│
└─ recommend/                 # Recommend 도메인 (패키지 ← 여기!)
├─ domain/
│  ├─ model/
│  │  └─ ids/
│  ├─ policy/
│  └─ exception/
│
├─ application/
│  ├─ service/
│  │  └─ policy/
│  ├─ port/
│  │  ├─ in/             # Use Case (Inbound Port)
│  │  └─ out/            # Repository, External (Outbound Port)
│  └─ dto/
│      └─ response/
│
└─ infrastructure/
├─ persistence/       # JPA Repository
│  ├─ entity/
│  ├─ repository/
│  └─ mapper/
│
├─ adapter/           # 외부 연동 Adapter
│  ├─ CourseAdapter.java      # Course BC 호출
│  ├─ EnrollmentAdapter.java  # Enrollment BC 호출
│  └─ MemberAdapter.java      # Member BC 호출
│
├─ batch/             # Spring Batch (유지)
│  ├─ config/
│  ├─ job/
│  └─ ...
│
├─ event/             # Spring Event Listener (RabbitMQ 대체)
│  ├─ RecommendEventListener.java
│  └─ payload/
│
├─ ai/                # AI 통합 모듈 (향후 추가)
│  ├─ AIClient.java
│  └─ dto/
│
└─ web/               # REST Controller
├─ RecommendController.java
└─ dto/
├─ request/
└─ response/


**AI 모델 통합 관점에서 모놀리식도 충분히 깔끔합니다.**

***

## 이유

### 1. AI는 "외부 시스템"이므로 패키지 구조와 무관

```
[LXP Monolith]  →  HTTP/REST  →  [AI Service (Python/FastAPI)]
                                   - 추천 모델
                                   - 예측 API
```

**AI 통합 방식**:
- `recommend/infrastructure/ai/AIClient.java` 에서 외부 API 호출
- 패키지든 모듈이든 상관없이 동일한 방식

**MSA든 모놀리식이든 동일**:
```java
@Service
public class AIRecommendService {
    private final RestTemplate restTemplate;
    
    public List<Course> getAIPredictions(String learnerId) {
        // AI 서비스 호출 (어디서든 동일)
        return restTemplate.postForObject(aiServiceUrl, request, Response.class);
    }
}
```

***

### 2. 패키지 분리로 논리적 경계 유지

```
src/main/java/com/lxp/recommend/
├─ ai/                    # AI 관련 코드만 격리
│  ├─ AIClient.java
│  ├─ AIModelConfig.java
│  └─ dto/
│     ├─ PredictionRequest.java
│     └─ PredictionResponse.java
│
├─ domain/                # 비즈니스 로직 (AI와 분리)
└─ infrastructure/
```

**장점**:
- AI 코드는 `recommend/ai/` 패키지에만 집중
- 다른 도메인(Course, Enrollment)과 완전히 독립
- 나중에 AI 로직 변경해도 recommend 폴더 내부만 수정

***

### 3. 모놀리식이 오히려 편한 점

**MSA 대비 장점**:
- AI 모델 응답을 DB에 저장할 때 **로컬 트랜잭션** 사용 가능
- 다른 BC 데이터를 조회할 때 **HTTP 오버헤드 없음**
- AI 결과를 Course/Member와 조인할 때 **SQL로 바로 처리**

**예시**:
```java
@Transactional
public void processAIPrediction(String learnerId) {
    // 1. AI 호출 (외부 API)
    AIResponse prediction = aiClient.predict(learnerId);
    
    // 2. Course BC 조회 (로컬 Service, 빠름!)
    List<Course> courses = courseService.findByIds(prediction.getCourseIds());
    
    // 3. 추천 결과 저장 (같은 트랜잭션)
    recommendRepository.save(recommendation);
    
    // ✅ 모두 하나의 트랜잭션 안에서 처리
}
```

***

### 4. 나중에 분리도 쉬움

**지금 패키지로 시작 → 나중에 MSA 전환 가능**:

```
Step 1: 패키지 분리 (지금)
src/main/java/com/lxp/recommend/

Step 2: 필요시 모듈 분리
lxp-recommend/ (독립 Gradle 모듈)

Step 3: 필요시 서버 분리
lxp-recommend-service/ (독립 서버)
```

**Port/Adapter 패턴 유지**하면 언제든 분리 가능합니다.

***

## 모듈러 모놀리스 vs 모놀리스 차이

| 항목 | 모듈러 모놀리스 (3차) | 모놀리스 (5차) | 차이 |
|------|---------------------|---------------|------|
| 물리 구조 | 별도 Gradle 모듈 | 패키지만 분리 | 빌드 단순화 |
| 논리 경계 | 강제 (의존성 제어) | 컨벤션 (팀 규칙) | 약간 느슨 |
| AI 통합 | 동일 | 동일 | **차이 없음** |
| 배포 | 단일 JAR | 단일 JAR | 동일 |

**핵심**: AI 통합 관점에서는 **차이가 거의 없습니다**.

***

## 권장사항

### AI 코드 격리 전략

```
recommend/
├─ domain/              # 비즈니스 로직
├─ application/         # Use Case
└─ infrastructure/
    ├─ ai/              # ✅ AI 전용 패키지
    │  ├─ AIClient.java
    │  ├─ AIModelType.java (enum)
    │  └─ dto/
    │
    ├─ adapter/         # 다른 BC 호출
    └─ persistence/     # DB 저장
```

**규칙**:
- AI 관련 코드는 `recommend/infrastructure/ai/`에만
- 다른 도메인과 섞이지 않도록 패키지 명확히 분리
- `README.md`에 "AI 모델은 외부 서비스로 분리됨" 명시

***

## 결론

- ✅ 모놀리식이어도 AI 통합은 깔끔하게 가능
- ✅ 패키지 분리로 논리적 독립성 유지
- ✅ 오히려 MSA보다 트랜잭션/성능 측면에서 유리
- ✅ 나중에 필요하면 분리 전환 가능

**걱정하지 마세요. 지금 구조가 맞습니다!** 🎯