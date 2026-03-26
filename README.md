# Spring Boot 게시판 CRUD 학습 프로젝트

## 프로젝트 개요
Spring Boot와 Spring Data JPA를 활용하여 **CRUD**가 어떻게 구현되고 동작하는지 이해하기 위한 백엔드 중심의 학습용 애플리케이션입니다.

---

## 기술 스택
* **Backend:** Java 17, Spring Boot 3.x, Spring Web, Spring Security
* **Database & ORM:** H2 Database, Spring Data JPA
* **Authentication:** JWT
* **Frontend:** HTML, Bootstrap, Thymeleaf

---

## 아키텍처 및 데이터 흐름
전체적인 CRUD 동작은 일반적인 3-Layered 아키텍처 패턴을 따릅니다.
1. **Client (Thymeleaf/JS)**: Fetch API를 통해 JSON 형식의 데이터와 JWT 토큰을 서버로 전송합니다.
2. **Controller (`@RestController`)**: 클라이언트의 HTTP 요청(GET, POST, PATCH, DELETE)을 받아 매핑하고, DTO(`@Valid` 검증 포함)를 통해 데이터를 Service 계층에 전달합니다.
3. **Service (`@Service`)**: 실질적인 비즈니스 로직(예: 작성자 권한 검증, 엔티티 조회 등)을 처리합니다.
4. **Repository (`@Repository`)**: Spring Data JPA의 `JpaRepository`를 상속받아 직접적인 SQL 작성 없이 데이터베이스와 상호작용합니다.

---

## 핵심 CRUD 구현 및 동작 원리

### 1. 도메인(Entity) 설계
* **Post (게시글)**: 제목(`title`), 내용(`content`), 작성자(`writer`) 필드로 구성됩니다.
* **Comment (댓글)**: 내용, 작성자, 그리고 **게시글과의 다대일(N:1) 관계**(`@ManyToOne`)를 가집니다.
* **JPA Auditing**: `@EnableJpaAuditing`과 `@EntityListeners`를 활용하여 데이터가 생성되거나 수정될 때 `createdAt`, `updatedAt` 시간이 DB에 자동으로 기록되도록 구현되었습니다.

### 2. 회원가입 및 로그인 (인증 로직)
CRUD 권한을 제어하기 위해 인증 기능이 선행됩니다.
* **회원가입 (`POST /auth/signup`)**: 입력받은 비밀번호를 `BCryptPasswordEncoder`로 단방향 암호화하여 DB에 저장합니다.
* **로그인 (`POST /auth/login`)**: 사용자가 아이디와 비밀번호를 전송하면 `AuthenticationManager`가 이를 검증합니다. 성공 시 `JwtTokenProvider`가 Access Token을 발급합니다. 클라이언트는 이를 LocalStorage에 저장하여, 이후 게시글 작성/수정/삭제 API 요청 시 `Authorization: Bearer <Token>` 헤더에 담아 보냅니다.

### 3. 게시글 (Post) CRUD 파헤치기
* **C (Create - 생성)**: `POST /posts`
    * `JwtAuthenticationFilter`를 통과한 인증된 사용자만 접근할 수 있습니다.
    * Security Context(`@AuthenticationPrincipal`)에서 현재 로그인한 사용자의 아이디(`username`)를 추출하여, 이를 게시글 엔티티의 작성자(writer) 필드에 자동으로 주입하고 DB에 `save()` 합니다.
* **R (Read - 조회)**: `GET /posts` (목록), `GET /posts/{id}` (상세)
    * 인증 없이 누구나 접근 가능합니다(`SecurityConfig`에서 `permitAll()` 처리).
    * DB에서 Entity 객체를 꺼내온 뒤 직접 반환하지 않고, `PostResponse`라는 **DTO(Data Transfer Object) 레코드**로 변환하여 클라이언트에 응답합니다. (엔티티 노출 방지)
* **U (Update - 수정)**: `PATCH /posts/{id}`
    * `PostService.update()` 내부에서 **데이터 소유권 검증(`validateWriter`)** 로직이 실행됩니다. 토큰의 주인이 게시글 작성자와 다르면 `PostAccessDeniedException` 예외가 발생합니다.
    * JPA의 **변경 감지(Dirty Checking)** 기능을 활용합니다. `save()`를 명시적으로 호출하지 않아도 조회한 엔티티 객체의 필드값(제목, 내용)만 변경해 주면, 트랜잭션 종료 시 자동으로 UPDATE 쿼리가 발생합니다.
* **D (Delete - 삭제)**: `DELETE /posts/{id}`
    * 수정과 마찬가지로 작성자 본인만 삭제할 수 있습니다.
    * 게시글 삭제 시 DB의 외래키 무결성 제약조건 오류를 방지하기 위해, **해당 게시글에 달린 댓글들을 먼저 일괄 삭제**(`commentRepository.deleteByPostId(id)`)한 뒤에 게시글을 삭제하도록 순서가 제어되어 있습니다.

### 4. 댓글 (Comment) CRUD
게시글의 CRUD와 논리적으로 유사하지만, **특정 게시글(부모)에 종속적**이라는 특징이 있습니다.
* URL 설계 또한 RESTful 원칙에 따라 `/posts/{postId}/comments` 형태의 계층적 구조를 가집니다.
* **생성 (`POST`)**: Path Variable로 넘어온 `postId`를 통해 부모 게시글이 실제로 존재하는지 확인(`orElseThrow`)한 후, 해당 게시글 객체를 댓글 엔티티에 연관지어 저장합니다.