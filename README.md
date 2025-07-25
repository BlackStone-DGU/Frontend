# BlackStone Frontend
블랙스톤 프론트엔드 파트 리포지토리입니다.

---

## 👥 팀원 소개

| 권오휘 | 최은서 | 황승민 |
|:---:|:---:|:---:|
| <img src="https://github.com/kimdh0521.png" width="180" height="180"> | <img src="https://github.com/Ireneldia.png" width="180" height="180"> | <img src="https://github.com/sxunxin.png" width="180" height="180"> |
| [@kimdh0521](https://github.com/kimdh0521) | [@Ireneldia](https://github.com/Ireneldia) | [@sxunxin](https://github.com/sxunxin) |

---

## ⚙️ Tech Stack

| 기술 | 설명 |
|------|------|
| Kotlin | 메인 프로그래밍 언어 |
| XML Layout | XML 기반 UI 설계 |
| Git | 체계적인 코드 관리 및 협업 |

---

## 🧭 Git Conventions

### 📌 Branch 전략

- 메인 브랜치: `main`
- 기능 개발 시 화면 또는 역할 기준으로 브랜치 명명 (영역/기능 형태)

**작업 흐름**  
1. 기능 이슈 생성 → 번호 발급  
2. `main` → `기능 브랜치` 생성 후 작업  
3. 작업 완료 → `main` 브랜치로 Pull Request 생성

### 📌 작업 템플릿 가이드

작업 유형에 따라 명확하게 커밋 타입을 구분합니다.

| 타입 | 용도 |
|------|------|
| **Feat** | 새로운 기능 추가 |
| **Fix** | 버그 수정 |
| **Refactor** | 코드 리팩토링 (동작 변화 없이 구조 개선) |
| **Docs** | 문서 작성 및 수정 (README, 주석 등) |
| **Style** | 코드 포맷, 네이밍, 세미콜론 등 스타일 변경 (기능 무관) |
| **Test** | 테스트 코드 추가 및 수정 |
| **Chore** | 설정, 빌드, 패키지 등 기타 변경 작업 |

---

### ✅ Commit 템플릿

```text
타입: 간단한 설명

- 작업한 내용에 대한 구체적인 설명
- 필요한 경우 여러 줄로 상세하게 작성
```

### 📝 Pull Request 템플릿

```txt
타입: 간단한 설명

## 작업 내용
- 무엇을 변경했는지 간단히 작성

## 참고 사항
- 리뷰 시 유의해야 할 사항
```

---

## 🧑‍💻 Code Style

본 프로젝트는 [Google Kotlin 스타일 가이드](https://developer.android.com/kotlin/style-guide?hl=ko)를 따르며, 일관된 네이밍 규칙을 유지합니다.

### 🔤 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| **Class** | PascalCase | `MainActivity`, `FeedViewModel` |
| **Function / Variable** | camelCase | `getFeedList()`, `currentPosition` |
| **Constant** | UPPER_SNAKE_CASE | `MAX_FEED`, `DEFAULT_TIME` |
| **Layout XML** | lowercase_snake_case | `activity_main.xml`, `item_feed.xml` |
| **Resource ID** | lowercase_snake_case | `btn_like`, `tv_title` |

### 📌 기타 규칙

- 콜백 함수는 `on` 접두어 사용 → 예: `onItemClick`, `onLoaded`
- 모든 소스 파일은 UTF-8 인코딩을 사용해야 합니다.
  
---

## 🧪 개발 환경

| 항목 | 정보 |
|------|------|
| min SDK | 28 |
| target SDK | 35 |
| IDE | Android Studio Meerkat (2024.3.1) |
| 테스트 기기 | Emulator : Medium Phone API 35 |

---
