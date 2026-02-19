---

# LXP Recommend BC - AI 기능 개발 명세서
**LangChain4j + Gemini 기반 기술 스택 확장 추천**

***

## 📋 문서 정보

| 항목 | 내용 |
|------|------|
| **프로젝트명** | LXP-BE Recommend BC AI 확장 |
| **작성일** | 2026-02-11 |
| **개발 기간** | 2주 (10 영업일) |
| **목표** | 태그 매칭 기반 추천 → AI 기반 기술 스택 확장 추천으로 고도화 |
| **담당** | Recommend BC Team |

***

## 🎯 프로젝트 목표

### 현재 상황 (As-Is)
**추천 방식**: 사용자 태그와 강좌 태그 직접 매칭
- 사용자 태그: `Java, Spring Boot, MySQL`
- 추천 결과: Java/Spring Boot가 태그에 포함된 강좌만 추천
- **한계**: 실무에서 함께 사용되는 Docker, AWS, Kubernetes 등 추천 불가

### 개선 방향 (To-Be)
**2-Tier 추천 아키텍처** 도입
- **Tier 1 (기존 유지)**: 명시적 태그 매칭 → 상위 6개
- **Tier 2 (신규 추가)**: AI 기반 기술 스택 확장 → 상위 4개
- **최종**: 중복 제거 후 10개 추천

### 기대 효과
1. **추천 다양성 증가**: 새로운 기술 영역 노출 (목표: 60% 이상)
2. **학습 경로 확장**: 백엔드 개발자 → 인프라/클라우드 학습 유도
3. **실무 맥락 반영**: "Spring Boot 개발자에게 Docker 배포 추천"

***

## 🏗️ 기술 스택

### 환경
- **Spring Boot**: 3.5.10
- **JDK**: 17
- **아키텍처**: Hexagonal Architecture (Port & Adapter 패턴 유지)
- **배치**: Spring Batch (기존 구조 활용)

### AI 통합
- **LLM 모델**: Google Gemini 1.5 Flash
- **프레임워크**: LangChain4j 0.35.0
- **통합 방식**: Declarative AI Service (인터페이스 정의 → 자동 구현)

### 의존성 추가
```gradle
dependencies {
// LangChain4j Core
implementation 'dev.langchain4j:langchain4j:0.35.0'
implementation 'dev.langchain4j:langchain4j-spring-boot-starter:0.35.0'

// Google Gemini
implementation 'dev.langchain4j:langchain4j-google-ai-gemini:0.35.0'
}
```

***

## 📁 프로젝트 구조

### 전체 디렉토리 맵

```
LXP-BE/recommend/
│
├─ domain/
│  ├─ model/
│  │  ├─ MemberRecommendation.java          # Aggregate Root (기존)
│  │  ├─ RecommendedCourse.java             # Entity (기존)
│  │  └─ PersonaType.java                   # 🆕 페르소나 Enum
│  │
│  └─ policy/
│     ├─ RecommendationScoringStrategy.java # 전략 인터페이스 (기존)
│     ├─ SimpleScoringStrategy.java         # Tier 1 구현 (기존)
│     └─ LangChainExpansionStrategy.java    # 🆕 Tier 2 구현 (AI)
│
├─ application/
│  ├─ service/
│  │  ├─ RecommendCommandService.java       # ⚙️ 2-Tier 통합 (수정)
│  │  ├─ RecommendQueryService.java         # 조회 (기존)
│  │  └─ PersonaDetectionService.java       # 🆕 역할 감지
│  │
│  ├─ port/
│  │  └─ required/
│  │     └─ KnowledgeExpansionPort.java     # 🆕 AI 추상화 Port
│  │
│  └─ dto/
│     ├─ PersonaContext.java                # 🆕 페르소나 컨텍스트
│     └─ ExpandedKeywords.java              # 🆕 AI 응답 DTO
│
└─ infrastructure/
├─ ai/                                    # 🆕 AI 레이어
│  ├─ config/
│  │  └─ GeminiConfig.java               # Gemini Bean 설정
│  │
│  ├─ service/
│  │  └─ KnowledgeExpanderService.java   # AI Service (LangChain4j)
│  │
│  ├─ adapter/
│  │  └─ GeminiRecommendAdapter.java     # Port 구현체
│  │
│  ├─ prompt/
│  │  ├─ PromptTemplateManager.java      # 프롬프트 관리
│  │  └─ PersonaPromptFactory.java       # 6개 페르소나 프롬프트
│  │
│  └─ model/
│     ├─ ExpandKeywordsRequest.java      # AI 요청 DTO
│     └─ ExpandKeywordsResponse.java     # AI 응답 DTO
│
├─ batch/
│  ├─ config/
│  │  └─ RecommendationBatchConfig.java  # 배치 설정 (기존)
│  │
│  └─ processor/
│     └─ RecommendationProcessor.java    # ⚙️ AI 통합 (수정)
│
├─ web/
│  └─ RecommendationController.java      # REST API (기존)
│
└─ persistence/
└─ repository/                         # JPA Repository (기존)
```

***

## 🔑 핵심 컴포넌트 설계

### 1. Domain Layer

#### **PersonaType.java** (신규 Enum)
**역할**: 학습자 역할 분류
```
페르소나 6종:
- BACKEND: 백엔드 개발자
- FRONTEND: 프론트엔드 개발자
- FULLSTACK: 풀스택 개발자 (MERN)
- DEVOPS: DevOps 지향 백엔드 개발자
- UIUX: UI/UX 디자이너
- AIML: AI/ML 엔지니어
```

#### **LangChainExpansionStrategy.java** (신규)
**역할**: Tier 2 점수 계산 전략
- `RecommendationScoringStrategy` 인터페이스 구현
- `KnowledgeExpansionPort`를 통해 AI 호출
- 확장 키워드와 강좌 태그 매칭 점수 계산
- **점수 범위**: 0.5~1.0 (Tier 1보다 낮게 설정)

***

### 2. Application Layer

#### **PersonaDetectionService.java** (신규)
**역할**: 사용자 태그 기반 페르소나 자동 감지

**알고리즘**:
```
입력: 사용자 태그 + 수강 중 강좌 태그 (합집합)

처리:
1. 6개 페르소나별 시그니처 태그 매칭 점수 계산
- BACKEND: Java(3점), Spring Boot(3점), Node.js(3점)...
- FRONTEND: React(3점), Vue(3점), Angular(3점)...
- DEVOPS: Docker(4점), Kubernetes(4점), Jenkins(4점)...

2. 최고점 페르소나 선택

3. Fallback: 감지 실패 시 DEFAULT (BACKEND) 사용

출력: PersonaType
```

#### **KnowledgeExpansionPort.java** (신규 Port)
**역할**: AI 기능 추상화 인터페이스
```java
public interface KnowledgeExpansionPort {
/**
* 사용자 태그 기반 기술 스택 확장 키워드 생성
*
* @param context 페르소나 컨텍스트 (태그, 레벨, 페르소나)
* @return 확장된 키워드 리스트
*/
List<String> expandTechStack(PersonaContext context);
}
```

#### **RecommendCommandService.java** (수정)
**기존**: SimpleScoringStrategy만 사용
**개선**: 2-Tier 통합

**처리 흐름**:
```
1. 학습자 데이터 수집 (프로필, 수강 이력, 강좌 목록)
2. 페르소나 감지 (PersonaDetectionService)
3. Tier 1 계산 (기존 태그 매칭) → 점수순 정렬
4. Tier 2 계산 (AI 확장 키워드 매칭) → 점수순 정렬
5. 상위 6개(Tier 1) + 상위 4개(Tier 2) 선택
6. 중복 제거 (같은 강좌 ID)
7. 최종 10개 DB 저장
```

***

### 3. Infrastructure - AI Layer

#### **GeminiConfig.java** (신규)
**역할**: Gemini ChatLanguageModel Bean 생성

**설정 항목**:
- API Key: 환경 변수 (`GEMINI_API_KEY`)
- Model: `gemini-1.5-flash` (빠른 응답 + 비용 효율)
- Temperature: `0.3` (일관성 우선, 창의성 낮춤)
- Max Tokens: `500` (JSON 응답에 충분)
- Timeout: `10초` (배치 안정성)

#### **KnowledgeExpanderService.java** (신규 AI Service)
**역할**: Gemini LLM 호출 및 응답 파싱

**특징**:
- LangChain4j **Declarative AI Service** 패턴 사용
- 인터페이스만 정의 → LangChain4j가 자동으로 구현체 생성
- Spring Bean으로 자동 등록

**메서드**:
```java
@Service
public interface KnowledgeExpanderService {

@SystemMessage("당신은 IT 학습 경로 추천 전문가입니다...")
@UserMessage("""
사용자는 현재 {{userTags}}를 학습 중인 {{level}} 레벨의 {{persona}} 개발자입니다.

다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을
3가지 카테고리로 추천해주세요:
...
응답 형식: JSON
""")
ExpandKeywordsResponse expand(
@V("userTags") String userTags,
@V("level") String level,
@V("persona") String persona
);
}
```

**응답 파싱**:
- Gemini JSON 응답 → `ExpandKeywordsResponse` 자동 변환
- 파싱 실패 시 빈 리스트 반환 (예외 처리)

#### **GeminiRecommendAdapter.java** (신규 Adapter)
**역할**: `KnowledgeExpansionPort` 구현체

**책임**:
- Application DTO → AI Service 파라미터 변환
- `PersonaContext` → `{userTags, level, persona}` 문자열
- AI Service 응답 → Application DTO 변환
- `ExpandKeywordsResponse` → `List<String>` 키워드 추출
- 예외 처리 및 Fallback (빈 리스트 반환)

#### **PromptTemplateManager.java** (신규)
**역할**: 프롬프트 템플릿 관리

**기능**:
- 6개 페르소나별 프롬프트 저장
- 동적 변수 치환 (`{{userTags}}`, `{{level}}`, `{{persona}}`)
- 프롬프트 버전 관리 (v1.0, 추후 A/B 테스트용)

#### **PersonaPromptFactory.java** (신규)
**역할**: PersonaType에 따른 프롬프트 생성

**6개 페르소나 프롬프트**:
1. **BACKEND**: 인프라/배포, 모니터링, 클라우드 (3개 카테고리)
2. **FRONTEND**: 상태 관리/성능, 테스트/품질, 빌드/배포
3. **FULLSTACK**: 인증/보안, 배포/호스팅, API 설계, DB 고급 (4개 카테고리)
4. **DEVOPS**: 컨테이너 오케스트레이션, CI/CD, 인프라 자동화, 모니터링
5. **UIUX**: 프론트엔드 기초, 디자인 시스템/협업, 프로토타이핑
6. **AIML**: 모델 배포, 딥러닝 프레임워크, 데이터 처리, 클라우드 ML

***

### 4. Infrastructure - Batch

#### **RecommendationProcessor.java** (수정)
**기존**: Tier 1 추천만 계산
**개선**: AI 호출 추가

**처리 흐름**:
```java
@Override
public Long process(Long learnerId) throws Exception {
// 1. 페르소나 감지
PersonaType persona = personaDetectionService.detect(learnerId);

// 2. 2-Tier 추천 계산 (AI 포함)
recommendCommandService.refreshRecommendations(learnerId);

return learnerId;
}
```

***

## 🔄 데이터 흐름

### 전체 프로세스 (배치 실행)

```
┌─────────────────────────────────────────────────────────┐
│  1. Spring Batch Scheduler (새벽 3시)                   │
└───────────────────┬─────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────┐
│  2. RecommendationJob 시작                              │
│     - LearnerIdReader: 200명 ID 조회                    │
│     - Chunk Size: 10명씩 처리                           │
└───────────────────┬─────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────┐
│  3. RecommendationProcessor (10명 Chunk)               │
│     ├─ MemberFacade: 학습자 프로필 조회                │
│     ├─ CourseFacade: 후보 강좌 조회                    │
│     ├─ 🆕 PersonaDetectionService: 역할 감지           │
│     │   → 태그 분석 → PersonaType 반환                 │
│     └─ RecommendCommandService 호출                    │
└───────────────────┬─────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────┐
│  4. RecommendCommandService (2-Tier 통합)              │
│                                                          │
│  [Tier 1: 기존 추천]                                    │
│  ├─ SimpleScoringStrategy                               │
│  ├─ 태그 직접 매칭 (Explicit + Implicit)               │
│  └─ 점수순 정렬 → 상위 6개 선택                        │
│                                                          │
│  [Tier 2: AI 확장 추천] 🆕                             │
│  ├─ LangChainExpansionStrategy                          │
│  ├─ KnowledgeExpansionPort 호출                        │
│  │   ↓                                                   │
│  │  GeminiRecommendAdapter                              │
│  │   ↓                                                   │
│  │  KnowledgeExpanderService (AI Service)               │
│  │   ↓                                                   │
│  │  Gemini API 호출 (동기)                              │
│  │   ↓                                                   │
│  │  JSON 파싱 → 확장 키워드 리스트                     │
│  │   ["Docker", "Kubernetes", "AWS", ...]               │
│  ├─ 확장 키워드와 강좌 태그 매칭                       │
│  └─ 점수순 정렬 → 상위 4개 선택                        │
│                                                          │
│  [통합]                                                  │
│  ├─ Tier 1 (6개) + Tier 2 (4개)                        │
│  ├─ 중복 제거 (같은 강좌 ID)                           │
│  └─ 최종 10개 선정                                      │
└───────────────────┬─────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────┐
│  5. RecommendationWriter                                │
│     - DB 저장 (member_recommendations)                  │
│     - 강좌 정보 비정규화 (recommended_course_items)    │
└─────────────────────────────────────────────────────────┘
```

***

## ⚙️ 설정 파일

### application.yml

```yaml
spring:
application:
name: lxp-recommend

# 기존 설정 유지
datasource:
url: jdbc:postgresql://localhost:5432/lxp_recommend
jpa:
hibernate:
ddl-auto: validate
batch:
job:
enabled: false

# 🆕 LangChain4j 설정
langchain4j:
google-ai-gemini:
api-key: ${GEMINI_API_KEY}             # 환경 변수
model-name: gemini-1.5-flash           # 빠른 응답 + 비용 효율
temperature: 0.3                       # 일관성 우선
max-output-tokens: 500                 # JSON 응답에 충분
timeout: 10s                           # 배치 안정성

# 🆕 추천 설정
recommend:
scoring:
strategy: hybrid                       # simple | langchain | hybrid
tier1-count: 6                         # 기존 추천 개수
tier2-count: 4                         # AI 확장 추천 개수

batch:
chunk-size: 10
cron: "0 0 3 * * *"                    # 매일 새벽 3시
```

***

## 📊 프롬프트 설계

### 페르소나별 프롬프트 예시

#### **BACKEND 개발자 프롬프트**

**System Message**:
```
당신은 IT 학습 경로 추천 전문가입니다.
백엔드 개발자가 실무 역량을 키우기 위해 필요한 기술을 추천합니다.
응답은 간결하고 구체적이어야 하며, JSON 형식을 엄격히 준수해야 합니다.
```

**User Message**:
```
사용자는 현재 {{userTags}}를 학습 중인 {{level}} 레벨의 백엔드 개발자입니다.

백엔드 개발자로서 다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을
3가지 카테고리로 추천해주세요:

1. **인프라/배포** (예: Docker, Kubernetes, CI/CD)
2. **모니터링/관측성** (예: Prometheus, Grafana, ELK)
3. **클라우드 플랫폼** (예: AWS, GCP, Azure)

각 카테고리당 2-3개 키워드만 제시하세요.
백엔드 워크플로우에서 직접 사용하는 기술에 집중해주세요.
사용자가 이미 학습 중인 기술({{userTags}})과 중복되지 않는 키워드를 제시하세요.

응답 형식: JSON
{
"인프라/배포": ["키워드1", "키워드2"],
"모니터링/관측성": ["키워드1", "키워드2", "키워드3"],
"클라우드 플랫폼": ["키워드1", "키워드2"]
}
```

**변수 바인딩 예시**:
- `{{userTags}}`: "Java, Spring Boot, MySQL"
- `{{level}}`: "MIDDLE"

**Gemini 응답 예시**:
```json
{
"인프라/배포": ["Docker", "Kubernetes", "Jenkins"],
"모니터링/관측성": ["Prometheus", "Grafana", "ELK Stack"],
"클라우드 플랫폼": ["AWS EC2", "AWS RDS", "Lambda"]
}
```

***

## 🧪 테스트 전략

### 1. 단위 테스트

#### **PersonaDetectionServiceTest**
```
목적: 페르소나 감지 로직 검증

테스트 케이스:
- 백엔드 태그만 → BACKEND 반환
- 프론트엔드 태그만 → FRONTEND 반환
- React + Node.js → FULLSTACK 반환
- Docker + Kubernetes 많음 → DEVOPS 반환
- 태그 부족 → DEFAULT (BACKEND) 반환
```

#### **LangChainExpansionStrategyTest**
```
목적: AI 확장 점수 계산 로직 검증

Mock 사용:
- KnowledgeExpansionPort Mock
- 확장 키워드 고정: ["Docker", "AWS"]
- 강좌 태그와 매칭 점수 계산 검증
```

***

### 2. 통합 테스트

#### **GeminiIntegrationTest**
```
목적: 실제 Gemini API 호출 검증

전제:
- 테스트용 API Key 설정
- 낮은 max-tokens (비용 절감)

검증:
- JSON 파싱 성공
- 카테고리 3-4개 존재
- 키워드 중복 없음
```

#### **BatchIntegrationTest**
```
목적: End-to-End 배치 실행 검증

시나리오:
1. 테스트 학습자 10명 생성
2. 배치 Job 수동 실행
3. DB 검증: 10명 추천 결과 저장 확인
4. Tier 2 추천 포함 여부 검증
```

***

## 📈 성능 예측

### 배치 실행 시간 계산

**전제**:
- 학습자 수: 200명
- Chunk Size: 10명
- Gemini API 응답 시간: 0.5초/요청

**계산**:
```
Chunk당 처리:
├─ Facade 호출: 0.3초
├─ Tier 1 계산: 0.3초
├─ 페르소나 감지: 0.05초
├─ Gemini 호출 (10명): 5초 (0.5초 × 10)
└─ Tier 2 계산: 0.2초
───────────────────
Chunk당 총 시간: 5.85초

총 실행 시간:
= (200명 ÷ 10) × 5.85초
= 20 Chunk × 5.85초
= 117초 (약 2분)

✅ 목표 (3분 이내) 달성
```

***

## 💰 비용 추정

### Gemini API 비용

**요금제**: Gemini 1.5 Flash
- 입력: $0.075 / 1M 토큰
- 출력: $0.30 / 1M 토큰

**일일 사용량**:
```
200명 × 1회 = 200 요청/일

프롬프트 크기:
- 입력: 150 토큰
- 출력: 100 토큰

일일 토큰:
- 입력: 200 × 150 = 30,000 토큰
- 출력: 200 × 100 = 20,000 토큰

일일 비용:
- 입력: 0.03M × $0.075 = $0.00225
- 출력: 0.02M × $0.30 = $0.006
- 합계: $0.00825/일

월 비용: $0.00825 × 30 = $0.25/월

✅ 매우 저렴 (월 $1 미만)
```

***

## 🛡️ 에러 핸들링

### 예외 시나리오 및 대응

#### 1. Gemini API 호출 실패
**원인**: 네트워크 에러, 타임아웃, Rate Limit
**대응**:
- Timeout 10초 설정
- 실패 시 빈 리스트 반환 → Tier 1 추천만 제공
- 로그 기록 (ERROR 레벨)

#### 2. JSON 파싱 실패
**원인**: Gemini 응답 형식 불일치
**대응**:
- 프롬프트에 JSON 형식 명시 강화
- Jackson 파싱 예외 catch
- Fallback: 빈 리스트 반환

#### 3. 페르소나 감지 실패
**원인**: 태그가 너무 적거나 특이한 조합
**대응**:
- DEFAULT 페르소나 (BACKEND) 사용
- 로그 기록 (WARN 레벨)

#### 4. 중복 키워드
**원인**: Gemini가 사용자 태그 포함하여 응답
**대응**:
- Adapter에서 중복 필터링
- `userTags`와 교집합 제거

***

## 📅 개발 일정 (2주)

### Week 1: AI 통합 기반 구축

| Day | 작업 | 산출물 |
|-----|------|--------|
| **1** | 의존성 추가, GeminiConfig 구현 | Bean 생성 확인 |
| **2** | Health Check, API 연결 테스트 | `model.generate("Hello")` 성공 |
| **3** | PersonaType Enum, PromptTemplateManager | 6개 프롬프트 초안 |
| **4** | PersonaPromptFactory 완성 | 프롬프트 템플릿 최종 |
| **5** | KnowledgeExpansionPort, GeminiRecommendAdapter | Port/Adapter 구조 완성 |

***

### Week 2: 추천 로직 통합

| Day | 작업 | 산출물 |
|-----|------|--------|
| **6** | PersonaDetectionService 구현 | 시그니처 태그 매핑 테이블 |
| **7** | PersonaDetectionService 테스트 | 6개 페르소나 감지 검증 |
| **8** | LangChainExpansionStrategy 구현 | Tier 2 점수 계산 로직 |
| **9** | RecommendCommandService 수정, Batch 통합 | 2-Tier 통합 완료 |
| **10** | 200명 배치 테스트, 로그 모니터링 | 프로덕션 배포 준비 |

***

## ✅ 완료 체크리스트

### 개발 단계

- [ ] **의존성**: LangChain4j + Gemini 추가
- [ ] **설정**: application.yml에 Gemini 설정
- [ ] **AI Service**: KnowledgeExpanderService 인터페이스 정의
- [ ] **Adapter**: GeminiRecommendAdapter 구현
- [ ] **프롬프트**: 6개 페르소나 프롬프트 작성
- [ ] **페르소나 감지**: PersonaDetectionService 구현
- [ ] **2-Tier 통합**: RecommendCommandService 수정
- [ ] **배치 통합**: RecommendationProcessor 수정

***

### 테스트 단계

- [ ] **단위 테스트**: PersonaDetectionService 6개 케이스
- [ ] **단위 테스트**: LangChainExpansionStrategy Mock
- [ ] **통합 테스트**: Gemini API 실제 호출
- [ ] **E2E 테스트**: 200명 배치 실행
- [ ] **성능 측정**: 실행 시간 3분 이내 확인
- [ ] **비용 측정**: 일일 Gemini API 비용 확인

***

### 배포 단계

- [ ] **환경 변수**: GEMINI_API_KEY 설정 (프로덕션)
- [ ] **보안**: API Key Git 제외 확인
- [ ] **로깅**: AI 호출 성공/실패 로그 추가
- [ ] **모니터링**: 배치 실행 시간 메트릭 추가
- [ ] **알림**: 배치 실패 시 Slack/Email 알림
- [ ] **문서화**: API 키 발급 가이드 작성

***

## 🎯 성공 지표

### 기능 지표
- ✅ 배치 실행 시간 **3분 이내**
- ✅ Gemini API 호출 성공률 **95% 이상**
- ✅ 페르소나 감지 정확도 **90% 이상**

### 품질 지표
- ✅ Tier 2 추천 중 **새로운 태그 비율 60% 이상**
- ✅ AI 추천 강좌 **클릭률 기존 추천 대비 80% 이상 유지**

### 비용 지표
- ✅ Gemini API 비용 **월 $1 이하**

***

## 🚨 리스크 및 완화 전략

| 리스크 | 영향도 | 완화 전략 |
|--------|--------|----------|
| **Gemini API 장애** | 중 | Fallback: Tier 1 추천만 제공 |
| **JSON 파싱 실패** | 중 | 프롬프트 강화 + 예외 처리 |
| **배치 시간 초과** | 낮 | Chunk Size 조정 (10 → 20) |
| **비용 초과** | 낮 | 일일 한도 설정 + 알림 |
| **페르소나 오감지** | 낮 | DEFAULT 사용 + 로그 분석 |

***

## 📚 참고 자료

- **LangChain4j 공식 문서**: https://docs.langchain4j.dev/
- **Gemini API 가이드**: https://ai.google.dev/gemini-api/docs
- **Spring Boot Integration**: https://docs.langchain4j.dev/tutorials/spring-boot-integration/

***

## 👥 팀 역할

| 역할 | 담당자 | 책임 |
|------|--------|------|
| **개발 리더** | (이름) | 전체 아키텍처, 코드 리뷰 |
| **AI 통합** | (이름) | LangChain4j + Gemini 통합 |
| **프롬프트 엔지니어** | (이름) | 6개 페르소나 프롬프트 작성 |
| **테스트** | (이름) | 단위/통합 테스트 작성 |
| **DevOps** | (이름) | 배포, 모니터링 설정 |

***

**