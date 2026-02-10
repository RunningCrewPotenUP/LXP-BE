
## MSA -> 모놀리식 체크리스트

### 코드 제거
- [ ] FeignClient 인터페이스 3개 제거
- [ ] infrastructure/messaging/ 폴더 제거
- [ ] infrastructure/external/user/ 폴더 제거
- [ ] LocalMockConfig.java 제거

### 코드 수정
- [ ] MemberApiAdapter → MemberAdapter (Service 직접 호출)
- [ ] CourseApiAdapter → CourseAdapter (Service 직접 호출)
- [ ] EnrollmentApiAdapter → EnrollmentAdapter (Service 직접 호출)

### 설정 변경
- [ ] external.* 설정 제거
- [ ] spring.rabbitmq.* 설정 제거
- [ ] Redis 설정 공통 모듈로 이동
- [ ] Passport 설정 공통 모듈로 이동

### 의존성 정리
- [ ] Feign Client 의존성 제거
- [ ] RabbitMQ 의존성 제거
- [ ] Redis 의존성 root에 추가
- [ ] Spring Batch 의존성 root에 추가

### 테스트
- [ ] 단위 테스트 실행 확인
- [ ] 통합 테스트 실행 확인
- [ ] 배치 작업 실행 확인
- [ ] 성능 영향 확인

***

----------
## 📊 통합 작업 우선순위 로드맵

### Phase 1: 외부 통신 제거 (🔴 1주)
```
1. FeignClient 3개 제거
2. Adapter를 직접 Service 호출로 교체
3. External DTO 제거 (내부 모델로 통일)
4. application.yml의 external.* 설정 제거
```

### Phase 2: 불필요 코드 정리 (🟡 3일)
```
1. infrastructure/messaging/ 제거
2. infrastructure/external/user/ 제거
3. LocalMockConfig 제거
4. RabbitMQ 의존성 제거
```

### Phase 3: 설정 통합 (🟡 2일)
```
1. Redis 설정 공통 모듈로 이동
2. Passport 설정 공통 모듈로 이동
3. application-recommend.yml 생성 (모듈 전용)
```

### Phase 4: 테스트 및 검증 (🟢 3일)
```
1. 단위 테스트 실행 (MockAdapterConfig 활용)
2. 통합 테스트 (실제 Service 호출)
3. 배치 작업 실행 확인
4. 성능 테스트 (배치 영향도 확인)
```
