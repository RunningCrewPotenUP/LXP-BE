# Course API Documentation

## 강좌 수정 API (PATCH /courses/{courseId})

### 개요
강좌의 정보를 부분적으로 수정합니다. 전송하지 않은 필드는 기존 값을 유지합니다.

### Request Body

```json
{
  "title": "수정된 강좌 제목",           // optional
  "description": "수정된 설명",          // optional
  "thumbnailUrl": "https://...",        // optional
  "level": "MIDDLE",                    // optional: JUNIOR, MIDDLE, SENIOR, EXPERT
  "tags": [1, 2, 3],                    // optional: 태그 ID 목록 (전체 교체)
  "sections": [...]                     // optional: 섹션 목록 (전체 교체)
}
```

### 수정 동작 방식

#### 1. 기본 정보 (title, description, thumbnailUrl, level)
- 전송된 필드만 수정됩니다.
- `null`이 아닌 값이 전송되면 해당 필드가 업데이트됩니다.

**예시: 제목만 수정**
```json
{
  "title": "새로운 제목"
}
```

#### 2. 태그 (tags)
- 전송 시 **전체 교체** 방식으로 동작합니다.
- 기존 태그는 모두 제거되고 새로운 태그 목록으로 대체됩니다.

**예시: 태그 교체**
```json
{
  "tags": [5, 6, 7]
}
```

#### 3. 섹션 및 강의 (sections)
- 전송 시 **전체 교체** 방식으로 동작합니다.
- 기존 섹션과 강의는 모두 삭제되고 새로운 구조로 대체됩니다.
- 섹션과 강의의 순서(order)는 배열의 순서대로 자동 부여됩니다.

**예시: 섹션 구조 전체 교체**
```json
{
  "sections": [
    {
      "title": "새로운 섹션 1",
      "lectures": [
        {
          "title": "새로운 강의 1",
          "videoUrl": "https://video.url/new1"
        }
      ]
    },
    {
      "title": "새로운 섹션 2",
      "lectures": [
        {
          "title": "새로운 강의 2",
          "videoUrl": "https://video.url/new2"
        }
      ]
    }
  ]
}
```

### 주의사항

1. **sections 필드를 전송하면 기존 섹션/강의가 모두 삭제됩니다.**
   - 일부 섹션만 수정하려면 기존 데이터를 포함하여 전체를 다시 전송해야 합니다.

2. **tags 필드를 전송하면 기존 태그가 모두 교체됩니다.**
   - 태그 추가/제거가 필요하면 전체 태그 목록을 다시 전송해야 합니다.

3. **기존 섹션/강의의 ID는 수정 시 무시됩니다.**
   - 수정 API에서 섹션/강의는 항상 새로 생성됩니다.

### Response

성공 시 수정된 강좌의 상세 정보가 반환됩니다.

```json
{
  "data": {
    "id": 1,
    "title": "수정된 강좌",
    "description": "...",
    "thumbnailUrl": "...",
    "level": {
      "key": "MIDDLE",
      "value": "미들"
    },
    "instructor": {
      "instructorId": 1,
      "name": "강사명"
    },
    "tags": [...],
    "durationInHours": 10,
    "sections": [...],
    "createdAt": "2026-01-01T00:00:00Z",
    "updatedAt": "2026-01-02T00:00:00Z"
  },
  "error": null
}
```

### 에러 코드

| 코드 | HTTP Status | 설명 |
|------|-------------|------|
| COURSE_NOT_FOUND | 404 | 강좌를 찾을 수 없음 |
| INVALID_LEVEL | 400 | 유효하지 않은 난이도 값 |
