# 📚 냉장GO

>  1인 가구를 위한 식재료 유통기한 관리 및 AI 레시피 추천 앱

<a href="https://play.google.com/store/apps/details?id=com.stone.fridge&hl=ko">앱 다운로드</a>

---

## 🧩 프로젝트 소개

- **개발 배경**
  - 졸업 작품
    
- **개발 기간**
  - 2025.02~2025.06
    
- **개발 인원**
  - 백엔드 개발자 2명
  - 프론트엔드 개발자 1명
    
- **나의 역할**
  - 전체 앱 개발
  - UI/UX 디자인
  
---

## 🖼 Preview

<img width="5640" height="2400" alt="Image" src="https://github.com/user-attachments/assets/929ef78a-3b95-4f72-a31b-c7a0b3586d1a" />
<img width="5640" height="2400" alt="Image" src="https://github.com/user-attachments/assets/b07b5bbc-8141-44b8-9e4c-8e22052ea368" />

---

## 🛠 기술 스택

| 구분 | 사용 기술 |
|------|----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM / Clean Architecture |
| Auth | JWT |
| Async | Coroutine / Flow |
| Network | Retrofit |
| Database | Room / DataStore |
| ETC | StompProtocol / ML kit / Firebase / Paging |

---

## 📱 주요 기능

- 카카오 로그인 기능
- 유통기한 등록 및 임박 알림 기능
- 등록된 음식 기반 AI 레시피 추천 기능
- 위치 기반 공동구매 게시판 기능
- 사용자간 실시간 채팅 기능

---

## 🧠 설계 및 구현 포인트

- 사용자 친화적 UX
- Recomposition 최소화

---

## 🚀 트러블 슈팅

<a href="https://velog.io/@wjdwlsdl321/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%9E%90%EB%8F%99-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85">자동 로그인 트러블 슈팅</a>

<a href="https://velog.io/@wjdwlsdl321/Android-%EC%83%88%EB%A1%9C%EC%9A%B4-%EA%B8%80%EA%B3%BC-%EC%88%98%EC%A0%95-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0">새로운 글과 수정 구현하기 및 최적화</a>

---

## 📈 결과 및 배운 점

이 앱을 개발하면서 가장 집중한 부분은 성능 최적화였습니다. 공식 문서와 GitHub의 다양한 안드로이드 프로젝트를 참고하며 Paging, Room DB, Datastore를 적극적으로 활용해 불필요한 네트워크 호출을 최소화했습니다. 또한 화면을 여러 컴포넌트로 분리하여 상태 변화가 없는 컴포넌트에서는 Recomposition이 일어나지 않도록 구성해 효율성을 높였습니다.

SideEffect 처리에도 특히 신경을 썼습니다. 자동 로그인 기능을 구현할 때 access token과 refresh token의 만료 시점을 구분해 각각 다른 로직을 적용함으로써 안정적인 사용자 경험을 제공할 수 있었습니다.

캡스톤 디자인을 마친 이후에는 유지보수와 확장성을 고려해 프로젝트를 멀티 모듈 구조로 리팩터링했습니다. 이 과정을 통해 모듈 구성, 의존성 관리, 레이어 구조에 대한 이해도가 크게 향상되었고 더 견고한 앱 구조를 구현할 수 있게 되었습니다.

---

## 🔮 향후 개선 사항

- [ ] 기능 확장
- [ ] 성능 개선
- [ ] 테스트 코드 추가
- [ ] UI/UX 개선
