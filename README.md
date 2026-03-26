## 개요
Spring Boot와 Spring Data JPA를 사용하여 **CRUD**가 데이터베이스와 어떻게 상호작용하는지 학습하기 위한 프로젝트입니다.

## CRUD 데이터 흐름
클라이언트의 요청부터 DB 저장까지 다음과 같은 3계층 구조를 거쳐 동작합니다.
1. **Controller (`@RestController`)**: 클라이언트의 HTTP 요청(URL, 메서드)을 받아 알맞은 처리를 지시합니다.
2. **Service (`@Service`)**: 데이터를 검증하고 가공하는 핵심 비즈니스 로직을 수행합니다.
3. **Repository (`@Repository`)**: Spring Data JPA를 통해 데이터베이스에 실제 SQL 쿼리를 실행합니다.

### 1. Create (게시글 작성)
* **API:** `POST /posts`
* **동작 방식:**
    1. 클라이언트가 입력한 제목과 내용이 `PostRequest`라는 DTO(데이터 전달 객체)에 담겨 컨트롤러로 들어옵니다.
    2. 서비스 계층에서 넘어온 데이터를 바탕으로 새로운 `Post` **엔티티(Entity) 객체를 생성**합니다.
    3. `postRepository.save(post)` 메서드를 호출하면, JPA가 내부적으로 `INSERT` SQL 쿼리를 생성하여 데이터베이스에 게시글을 저장합니다.

### 2. Read (게시글 조회)
* **API:** `GET /posts` (목록 조회), `GET /posts/{id}` (상세 조회)
* **동작 방식:**
    1. **목록 조회:** `postRepository.findAll()`을 호출하여 DB에 있는 모든 게시글 엔티티(`SELECT *`)를 가져옵니다.
    2. **상세 조회:** URL 경로에 포함된 게시글 번호(`id`)를 가져와 `postRepository.findById(id)`로 특정 게시글 하나만 조회합니다.
    3. **DTO 변환 (중요):** DB에서 꺼낸 `Post` 엔티티 객체를 그대로 클라이언트에게 반환하지 않고, 응답 전용 객체인 `PostResponse`로 변환하여 꼭 필요한 데이터(제목, 내용, 작성자, 작성일)만 전달합니다.

### 3. Update (게시글 수정)
* **API:** `PATCH /posts/{id}`
* **동작 방식:**
    1. 먼저 `findById(id)`를 사용해 수정할 대상 게시글을 DB에서 조회하여 가져옵니다.
    2. 가져온 게시글 엔티티의 값을 새로운 제목과 내용으로 변경합니다. (`post.setTitle()`, `post.setContent()`)
    3. **변경 감지 (Dirty Checking):** 코드상에는 별도의 `update()`나 `save()` 메서드를 호출하는 부분이 없지만, JPA는 트랜잭션이 끝날 때 조회했던 객체의 값이 변경되었음을 감지하고 자동으로 `UPDATE` 쿼리를 DB에 날립니다.

### 4. Delete (게시글 삭제)
* **API:** `DELETE /posts/{id}`
* **동작 방식:**
    1. 삭제할 게시글을 DB에서 찾습니다.
    2. **연관 데이터 처리:** 게시글을 삭제하기 전에, 해당 게시글에 달려 있는 **댓글들을 먼저 모두 삭제**(`commentRepository.deleteByPostId(id)`)합니다. (데이터베이스 외래키 충돌 방지)
    3. 최종적으로 `postRepository.delete(post)`를 호출하여 DB에서 게시글을 완전히 삭제(`DELETE` 쿼리 실행)합니다.