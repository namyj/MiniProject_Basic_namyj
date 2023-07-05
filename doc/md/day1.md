# 6/29 - 1일차 중고 물품 관리

<aside>
💡 DAY 1️⃣ **중고 물품 관리** **요구사항 `6/29`**

1. 누구든지 중고 거래를 목적으로 물품에 대한 정보를 등록할 수 있다. 
    1. 이때 반드시 포함되어야 하는 내용은 ****************************************************************************제목, 설명, 최소 가격, 작성자****************************************************************************이다.
    2. 또한 사용자가 물품을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
    3. 최초로 물품이 등록될 때, 중고 물품의 상태는 **판매중** 상태가 된다.
2. 등록된 물품 정보는 누구든지 열람할 수 있다. 
    1. 페이지 단위 조회가 가능하다.
    2. 전체 조회, 단일 조회 모두 가능하다.
3. 등록된 물품 정보는 수정이 가능하다. 
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
4. 등록된 물품 정보에 이미지를 첨부할 수 있다.
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
    2. 이미지를 관리하는 방법은 자율이다.
5. 등록된 물품 정보는 삭제가 가능하다.
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
</aside>

### Schema

![Untitled](doc/image/day1/item_erd.png)

### 물품 정보 API

- 등록 (create)
- 전체 & 페이지 조회
- 단일 조회 (findById)
- 정보 수정 (update)
- 이미지 수정 (update)
- 삭제 (delete)

### 아이템 등록

- 필수 입력 필드 : ****************************************************************************제목, 설명, 최소 가격, 작성자, 패스워드 > 유효성 검증**
- 최초 등록 시, status = “판매중” 설정

- `POST /items`
    
    Request Body:
    
    ```json
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1000000,
        "writer": "jeeho.dev",
        "password": "1qaz2wsx"
    }
    ```
    
    Response Status: 200
    
    Response Body:
    
    ```json
    {
        "message": "등록이 완료되었습니다."
    }
    ```
    

### 단일 조회

- id로 한 아이템 조회
- 해당 아이템이 없을 경우 > ItemNotFoundException 발생
- `GET /items/{itemId}`
    
    Request Body: 없음
    
    Response Status: 200
    
    Response Body: 
    
    ```json
    {
    		"title": "중고 맥북 팝니다",
    		"description": "2019년 맥북 프로 13인치 모델입니다",
    		"minPriceWanted": 1000000,
        "status": "판매중"
    }
    ```
    

### 페이지 조회

- pageable 객체 사용
- page, limit 값을 전달하지 않을 경우 > 전체 조회 결과 출력

이슈

- 전체 조회 시 page, limit 인자가 없어서 발생하는 오류?
    - required = false 로 필수 인자가 아님을 명시
    - 해당 인자들이 들어오지 않으면, limit을 전체 데이터 개수로 해서 모두 가져오게 함
- Page 객체의 모든 정보가 출력되는 것을 어떻게 변경?

- `GET /items?page={page}&limit={limit}`
    
    Request Body: 없음
    
    Response Status: 200
    
    Response Body: 
    
    ```json
    {
        "content": [
    	      {
                "id": 1,
    						"title": "중고 맥북 팝니다",
    						"description": "2019년 맥북 프로 13인치 모델입니다",
    						"minPriceWanted": 1000000,
                "status": "판매중"
            },
    	      {
                "id": 2,
    						"title": "콜드브루 드립기 팝니다",
    						"description": "ㅈㄱㄴ",
    						"minPriceWanted": 20000,
                "imageUrl": "/static/images/image.png",
                "status": "판매완료"
            },
            // ...
        ],
        "totalPages": 4,
        "totalElements": 100,
        "last": false,
        "size": 25,
        "number": 1,
        "numberOfElements": 25,
        "first": false,
        "empty": false
    }ㄴㄴ
    ```
    

### 물품 정보 수정

- 처음 등록한 비밀번호를 첨부해서 정보 수정
- 비밀번호가 일치하지 않을 경우 > PasswordNotCorrectException 발생
- 추가) writer 와 password 둘 다 동일해야 동작 수행

- `~~PUT /items/{itemId}~~`
    
    Request Body:
    
    ```json
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1250000,
        "writer": "jeeho.dev",
        "password": "1qaz2wsx"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "물품이 수정되었습니다."
    }
    ```
    

### 물품 이미지 수정

- 처음 등록한 비밀번호를 첨부해서 이미지 수정 > Multipart
    - 이미지 로컬 파일 시스템에 저장
- 비밀번호가 일치하지 않을 경우 > PasswordNotCorrectException 발생
- 추가) writer 와 password 둘 다 동일해야 동작 수행

- `PUT /items/{itemId}/image`
    
    Request Body (Form Data):
    
    ```
    image:    image.png (file)
    writer:   jeeho.dev
    password: 1qaz2wsx
    ```
    
    Response Body:
    
    ```json
    {
        "message": "이미지가 등록되었습니다."
    }
    ```
    

### 삭제

- 처음 등록한 비밀번호를 첨부해서 정보 삭제
- 비밀번호가 일치하지 않을 경우 > PasswordNotCorrectException 발생
- 추가) writer 와 password 둘 다 동일해야 동작 수행

- `DELETE /items/{itemId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.dev",
        "password": "1qaz2wsx"
    }
    ```
    
    Response Body: 
    
    ```json
    {
        "message": "물품을 삭제했습니다."
    }
    ```