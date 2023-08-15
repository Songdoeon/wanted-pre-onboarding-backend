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
  - GET `/api/article/{article_id}`
- 게시글 수정 엔드포인트
  - PATCH `/api/article/{article_id}`
- 게시글 삭제 엔드포인트
  - DELETE `/api/article/{article_id}`
- 게시글 목록 조회 엔드포인트
  - GET `/api/article/pageable`

## 3️⃣ 데이터베이스 테이블 구조
<img width="519" alt="스크린샷 2023-08-15 오후 10 38 39" src="https://github.com/Songdoeon/wanted-pre-onboarding-backend/assets/96420547/363705bc-c631-499d-99ac-efd1b5e31efd">

## 5️⃣ 구현한 API 의 동작을 촬영한 데모 영상 링크
[https://youtu.be/lj4CmyZeZgk](https://youtu.be/lj4CmyZeZgk)

## 6️⃣ 구현 방법 및 이유에 대한 간략한 설명
- 생성일과 수정일을 자동화하기 위해 JPA Auditing 을 사용했습니다.
- 객체의 생성을 제어하기 위해 Static Method 를 사용했습니다.
- 데이터 보존을 위해 Soft Delete 방식을 사용했습니다.
- Dynamic Query Building 을 위해 Specification 을 정의했습니다.
- 불변성을 유지하여 객체의 안정성을 높이기 위해 Setter 를 사용하지 않았습니다.
- 뷰(View)와 비즈니스 로직을 분리하기 위해 Dto 를 사용했습니다.

### 설계에 대한 고민
- 하나의 디렉터리 안에 여러 도메인에서 사용하는 다양한 클래스가 모이는 것을 방지하기 위해 도메인형 디렉터리 구조를 선택했습니다.
- DRY 원칙을 준수하기 위해 패키지 이름과 클래스 이름의 중복을 피했습니다.
- 인터페이스와 구현체 클래스 사이의 관계가 1:1의 관계로 구성있기 때문에 Service, ServiceImpl 구조(관습적인 추상패턴)를 사용하지 않았습니다.
- RESTful API 설계 가이드를 준수했습니다.
  - URL Rules 
    - 마지막에 `/` 를 포함하지 않는다.
    - 소문자를 사용한다.
    - `_` 대신 `-` 를 사용한다.
    - 행위는 URL 에 포함하지 않는다.
  - Use HTTP methods
    - POST, GET, PUT, DELETE 4가지 methods 는 반드시 제공한다.
  - Use HTTP status
    - 의미에 맞는 HTTP status 를 리턴한다.

### 배포
- 각각의 서비스들을 Docker 이미지로 만들고, Docker Compose 를 이용하여 이를 통합적으로 실행 및 관리했습니다.
- Docker Hub 에 이미지를 push 한 뒤, 배포 서버에서 pull 하여 사용했습니다.
- 개발 및 배포 환경에서 다르게 실행될 수 있도록 Dockerfile 에 프로파일을 설정했습니다.

## 7️⃣ API 명세 (request/response 포함)
### ✅ 회원가입
```text
POST /auth/sign-up
Host : 43.202.116.187:8080
Content-type : application/json
```
- 이메일과 비밀번호로 회원가입할 수 있는 엔드포인트입니다.
- 이메일과 비밀번호에 대한 유효성 검사를 진행합니다.
- 비밀번호는 반드시 암호화하여 저장합니다.

#### Request Body
```json
{
  "email" : "test@gmail.com",
  "password" : "test1234"
}
```
|    Name     | Type  | Description | Nullable | Constraint |
|:-----------:|:-----:|:-----------:|:--------:|:--------:|
|  **email**  | String|이메일|  false   |  `@` 포함   |
|**password** | String|비밀번호|  false   |   8자 이상   |

#### Response
```json
{
  "response": "OK",
  "message": "회원가입에 성공했습니다"
}
````

#### Error
```json
{
    "status": 409,
    "message": "이미 존재하는 사용자입니다",
    "code": "ME-02-01",
    "timestamp": "2023-08-06T17:27:10.526399"
}
```

### ✅ 로그인
```text
POST /auth/sign-in
Host : 43.202.116.187:8080
Content-type : application/json
```
- 이메일과 비밀번호로 로그인할 수 있는 엔드포인트입니다.
- 사용자가 올바른 이메일과 비밀번호를 제공하면, 사용자 인증을 거친 후에 JWT(JSON Web Token)를 생성하여 사용자에게 반환합니다.
  - 로그인 한 시점부터 모든 API 요청에 access token 을 `Authorization header` 에 포함해야 합니다.
  - access token 의 유효 시간은 2시간이며, refresh token 의 유효 기간은 2주(14일)입니다.
- 이메일과 비밀번호에 대한 유효성 검사를 진행합니다.

#### Request Body
```json
{
  "email" : "test@gmail.com",
  "password" : "test1234"
}
```
|    Name     | Type  | Description | Nullable | Constraint |
|:-----------:|:-----:|:-----------:|:--------:|:--------:|
|  **email**  | String|이메일|  false   |  `@` 포함   |
|**password** | String|비밀번호|  false   |   8자 이상   |


#### Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZUBlZSIsInJvbGUiOiJST0xFX1VT*****"
}
````

#### Error
```json
{
  "status": 404,
  "message": "존재하지 않는 사용자입니다",
  "code": "ME-01-01",
  "timestamp": "2023-08-06T17:27:10.526399"
}
```

### ✅ access token 재발급
```text
POST /auth/re-issue
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- access token 을 재발급하는 엔드포인트입니다.
- access token 이 만료된 경우, redis 의 refresh token 을 이용하여 access token 을 재발급합니다.
  - refresh token 또한 만료되거나 존재하지 않을 시, 로그아웃 된 것과 같은 상태입니다.
- access token 이 만료되지 않은 경우, 동일한 access token 을 반환합니다.

#### Request Body
```json
{
  "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZUBlZSIsInJvbGUiOiJST0xFX1VT*****"
}
```

|    Name     | Type  | Description | Nullable |
|:-----------:|:-----:|:-----------:|:--------:|
|  **accessToken**  | String|  엑세스토큰      |  false   |

#### Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZUBlZSIsInJvbGUiOiJST0xFX1VT*****"
}
````

#### Error
```json
{
  "status": 401,
  "message": "유효하지 않은 엑세스 토큰입니다",
  "code": "AU-05-01",
  "timestamp": "2023-08-06T17:27:10.526399"
}
```

### ✅ 로그아웃
```text
POST /auth/logout
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 로그아웃을 하는 엔드포인트입니다.
- 로그아웃 시 redis 의 refresh token 이 삭제됩니다.

#### Response
```json
{
  "response": "OK",
  "message": "로그아웃에 성공했습니다"
}
````

### ✅ 게시글 생성
```text
POST /post
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 새로운 게시글을 생성하는 엔드포인트입니다.

#### Request Body
```json
{
  "title" : "test_title",
  "content" : "test_content"
}
```
|    Name     | Type  | Description | Nullable |
|:-----------:|:-----:|:-----------:|:--------:|
|  **title**  | String|  제목      |  false   |
|  **content**  | String|  내용      |  false   |

#### Response
```json
{
  "response": "OK",
  "message": "게시글 생성에 성공했습니다"
}
````

### ✅ 게시글 수정
```text
PUT /post/{post-id}
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 ID와 수정 내용을 받아 해당 게시글을 수정하는 엔드포인트입니다.
- 게시글을 수정할 수 있는 사용자는 게시글 작성자이어야 합니다.

#### Request Body
```json
{
  "content" : "updated_content"
}
```

|    Name     | Type  | Description |  Nullable  |
|:-----------:|:-----:|:-----------:|:----------:|
|  **title**  | String|   제목        |    true    |
|**content** | String|     내용      |    true    |


#### Response
```json
{
  "response": "OK",
  "message": "게시글 수정에 성공했습니다"
}
````

#### Error
```json
{
  "status": 404,
  "message": "게시글 접근 권한이 없습니다",
  "code": "PO-02-01",
  "timestamp": "2023-08-06T18:12:18.762115"
}
````

### ✅ 게시글 삭제
```text
DELETE /post/{post-id}
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 ID를 받아 해당 게시글을 삭제하는 엔드포인트입니다.
- 게시글을 삭제할 수 있는 사용자는 게시글 작성자만이어야 합니다.

#### Response
```json
{
  "response": "OK",
  "message": "게시글 삭제에 성공했습니다"
}
````

#### Error
```json
{
  "status": 404,
  "message": "게시글 접근 권한이 없습니다",
  "code": "PO-02-01",
  "timestamp": "2023-08-06T18:12:18.762115"
}
```

### ✅ 게시글 단건 조회
```text
GET /pet/{post-id}
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글의 ID를 받아 해당 게시글을 조회하는 엔드포인트입니다.

#### Response
```json
{
  "author": "test@gmail.com",
  "title": "test_title",
  "content": "test_content",
  "createDate": "2023-08-05T11:13:05.622013",
  "updateDate": "2023-08-05T11:13:05.622013"
}
````

#### Error
```json
{
    "status": 404,
    "message": "존재하지 않는 게시글입니다",
    "code": "PO-01-01",
    "timestamp": "2023-08-06T13:51:55.506066"
}
```

### ✅ 게시글 목록 조회
```text
POST /pet/profile
Host : 43.202.116.187:8080
Authorization : Bearer ${ACCESS_TOKEN}
Content-type : application/json
```
- 게시글 목록을 조회하는 엔드포인트입니다.
- Pagination 이 적용되어 있습니다.

#### Request Params
```text
/post/list?pageNo=0&pageSize=5
```
|    Name     |  Type  | Description | Nullable | Default Value |
|:-----------:|:------:|:-----------:|:--------:|:--------:|
|  **pageNo**  |  int   |   페이지 번호    |   true   | 0 |
|  **pageSize**  |  int   |   페이지 사이즈   |   true   | 3 |
|  **sortBy**  | String |    정렬 기준    |   true   | `createDate` |

#### Response
```json
{
  "content": [
    {
      "author": "test@gmail.com",
      "title": "test_title_1",
      "createDate": "2023-08-05T11:13:05.622013"
    },
    {
      "author": "test@gmail.com",
      "title": "test_title_2",
      "createDate": "2023-08-05T10:41:09.715076"
    }
  ],
  "pageNo": 0,
  "pageSize": 3,
  "totalElements": 2,
  "totalPages": 1,
  "last": true
}
````
