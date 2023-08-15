# wanted-pre-onboarding-backend

- 사용한 기술 스택
  - Java/Spring Boot
- 데이터 저장소
  - MySQL 5.7
- 과정
  - [x] 단위 테스트 작성
  - [x] docker-compose를 이용하여 애플리케이션 환경을 구성

## 지원자의 성명
송도언

## 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)
### 애플리케이션 실행 방법
```bash
$ docker pull songdo/wanted-pre-onboarding-backend
$ docker-compose up
```

### 엔드포인트 호출 방법
- 회원가입 엔드포인트
  - POST `/sign-up`
- 로그인 엔드포인트
  - POST `/login`
- 게시글 생성 엔드포인트
  - POST `/api/article/register`
- 게시글 조회 엔드포인트
  - GET `/api/article/{articleId}`
- 게시글 수정 엔드포인트
  - PATCH `/api/article/{articleId}`
- 게시글 삭제 엔드포인트
  - DELETE `/api/article/{articleId}`
- 게시글 목록 조회 엔드포인트
  - GET `/api/article/pageable`

## 데이터베이스 테이블 구조
<img width="519" alt="스크린샷 2023-08-15 오후 10 38 39" src="https://github.com/Songdoeon/wanted-pre-onboarding-backend/assets/96420547/363705bc-c631-499d-99ac-efd1b5e31efd">

## 구현한 API 의 동작을 촬영한 데모 영상 링크
[https://youtu.be/lj4CmyZeZgk](https://youtu.be/lj4CmyZeZgk)

## 구현 방법 및 이유에 대한 간략한 설명
- 코드 관리를 위한 단위테스트 작성
- Spring security를 이용한 JWT 사용
- Entity 영속성 관리를 위한 DTO 생성
- 생성일과 수정일을 일괄 관리를 위해 baseTime 및 JPA Auditing 사용
- 예외처리 구조화

## Docker
- App을 Docker 이미지로 만들고 Database는 기본 이미지를 받아 docker-compose를 이용하여 관리
- Docker Hub 에 이미지를 push 완료
- App 이미지 pull 이후 docker-compose.yml을 이용하여 실행가능

## API 명세 (request/response 포함)
### 회원가입
```text
POST /sign-up
Host : 127.0.0.1:8080
Content-type : application/json
```
- 이름과 비밀번호 valid 진행
- 비밀번호 Encoding 후 저장

### Request Body
```json
{
  "username" : "song@wanted.com",
  "password" : "12341234"
}
```

#### Response
```json
{
  "response": "OK",
  "message": "회원가입완료"
}
```

#### Error
```json
{
    "code": "BINDING_ERROR",
    "message": "아이디는 이메일 형식이어야합니다."
}
```

- 이름에 @가 들어가지 않을경우

#### Error
```json
{
    "code": "BINDING_ERROR",
    "message": "비밀번호는 8자 이상이어야합니다."
}
```
- 비밀번호가 8자리 미만인 경우

### 로그인
```text
POST /login
Host : 127.0.0.1:8080
Content-type : application/json
```
- 아이디와 비밀번호로 로그인
- 사용자가 올바른 이메일과 비밀번호를 제공하면, PrincipalDetailService를 거친후 JWT를 생성하여 헤더에 담아 전송합니다
- api 요청엔 JWT 헤더가 담겨있어야합니다
- token 유효기간은 10분입니다.

### Request Body
```json
{
  "email" : "song@wanted.com",
  "password" : "12341234"
}
```

### Error
```json
{
    "timestamp": "2023-08-15T23:09:20.634+00:00",
    "status": 401,
    "error": "Unauthorized",
    "path": "/login"
}
```

### 게시글 생성
```text
POST /api/article/register
Host : 127.0.0.1:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 새로운 게시글을 생성

#### Request Body
```json
{
  "title" : "원티드 게시판 제목",
  "content" : "원티드 프리온보딩"
}
```

#### Response
{article_id}

### 게시글 조회
```text
GET /api/article/{articleId}
Host : 127.0.0.1:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 ID를 이용한 게시글 조회

#### Response
```json
{
    "id": 1,
    "title": "원티드 게시판 제목",
    "content": "원티드 프리온보딩",
    "writer": "song@wanted.com",
    "createdDate": "2023-08-15T23:23:16.499973",
    "updatedDate": "2023-08-15T23:23:16.499973"
}
````

#### Error
```json
{
    "code": "NOT_FOUND_ARTICLE",
    "message": "Article Not Found Error"
}
```

### 게시글 수정
```text
PATCH /api/article/{articleId}
Host : 127.0.0.1:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 제목과 내용을 다시 입력받아 내용을 변경합니다.
- 게시글을 작성자와 현재 로그인한 사용자의 아이디를 비교합니다.

#### Request Body
```json
{
  "title" : "updated_title",
  "content" : "updated_content"
}
```

#### Response
{
    "id": 1,
    "title": "원티드 게시판 제목",
    "content": "원티드 프리온보딩",
    "writer": "song@wanted.com",
    "createdDate": "2023-08-15T23:23:16.499973",
    "updatedDate": "2023-08-15T23:23:16.499973"
}

#### Error
```json
{
    "code": "NOT_MATCHED_WRITER",
    "message": "Not Matched Writer"
}
```

### 게시글 삭제
```text
DELETE /api/article/{articleId}
Host : 127.0.0.1:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 ID를 이용하여 해당 게시글을 삭제
- 게시글을 작성자만 삭제가 가능

#### Response
```json
{article_id}
```

#### Error
```json
{
    "code": "NOT_MATCHED_WRITER",
    "message": "Not Matched Writer"
}
```

### 게시글 목록 조회
```text
POST /api/article/pageable
Host : 127.0.0.1:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글 목록 페이징 처리

#### Request Params
```text
/api/article/pageable?page=0&size=5
```

#### Response
```json
[
    {
        "id": 1,
        "title": "원티드 게시판 제목",
        "content": "원티드 프리온보딩",
        "writer": "song@wanted.com",
        "createdDate": "2023-08-15T23:23:16.499973",
        "updatedDate": "2023-08-15T23:23:16.499973"
    },
    {
        "id": 2,
        "title": "원티드 게시판 제목",
        "content": "원티드 프리온보딩",
        "writer": "song@wanted.com",
        "createdDate": "2023-08-15T23:23:19.016098",
        "updatedDate": "2023-08-15T23:23:19.016098"
    },
    {
        "id": 3,
        "title": "원티드 게시판 제목",
        "content": "원티드 프리온보딩",
        "writer": "song@wanted.com",
        "createdDate": "2023-08-15T23:23:19.604681",
        "updatedDate": "2023-08-15T23:23:19.604681"
    }
]
```
