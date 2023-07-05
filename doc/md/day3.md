# 7/5 - 3일차 구매 제안 관리

<aside>
💡 DAY **3️⃣ 구매 제안 요구사항 `7/4`**

1. 등록된 물품에 대하여 구매 제안을 등록할 수 있다. 
    1. 이때 반드시 포함되어야 하는 내용은 **대상 물품, 제안 가격, 작성자**이다.
    2. 또한 구매 제안을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
    3. 구매 제안이 등록될 때, 제안의 상태는 “**제안”** 상태가 된다.
2. 구매 제안은 대상 물품의 주인과 등록한 사용자만 조회할 수 있다.
    1. **대상 물품의 주인**은, 대상 물품을 등록할 때 사용한 **작성자와 비밀번호**를 첨부해야 한다. 이때 물품에 등록된 모든 구매 제안이 확인 가능하다. 페이지 기능을 지원한다.
    2. **등록한 사용자**는, 조회를 위해서 자신이 사용한 **작성자와 비밀번호**를 첨부해야 한다. 이때 자신이 등록한 구매 제안만 확인이 가능하다. 페이지 기능을 지원한다.
3. 등록된 제안은 수정이 가능하다. 
    1. 이때, 제안이 등록될 때 추가한 **작성자와 비밀번호**를 첨부해야 한다.
4. 등록된 제안은 삭제가 가능하다. 
    1. 이때, 제안이 등록될 때 추가한 **작성자와 비밀번호**를 첨부해야 한다.
5. 대상 물품의 주인은 구매 제안을 수락할 수 있다. 
    1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 **작성자와 비밀번호**를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 “**수락”**이 된다.
6. 대상 물품의 주인은 구매 제안을 거절할 수 있다. 
    1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 **작성자와 비밀번호**를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 “**거절”**이 ****된다.
7. 구매 제안을 등록한 사용자는, 자신이 등록한 제안이 수락 상태일 경우, 구매 확정을 할 수 있다. 
    1. 이를 위해서 제안을 등록할 때 사용한 **작성자와 비밀번호**를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 **확정** 상태가 된다.
    3. 구매 제안이 확정될 경우, 대상 물품의 상태는 **판매 완료**가 된다.
    4. 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 **거절**이 된다.
</aside>

### Schema

![Offer](doc/image/day3/offer_erd.png)

Offer

### 구매 제안 API

- 제안 등록
- 제안이 등록된 item의 writer의 동작
    - 제안 조회
    - 제안 수락
    - 제안 거절
- 제안을 등록한 writer의 동작
    - 제안 조회
    - 제안 수정
    - 제안 삭제
    - 제안 확정

### 제안 등록

- 필수 항목: **대상 물품(itemId), 제안 가격, 작성자, 비밀번호**
- 구매 제안이 등록될 때, 제안의 상태는 “**제안”** 상태가 된다.
- 추가) item이 존재해야 제안을 등록할 수 있음

- `POST /items/{itemId}/offers`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234",
        "suggestedPrice": 1000000
    }
    ```
    
    Response Status: 200
    
    Response Body:
    
    ```json
    {
        "message": "구매 제안이 등록되었습니다."
    }
    ```
    

### 제안 조회 - offer writer, item writer

- 작성자 & 비밀번호를 첨부해서 조회
- 해당 제안을 등록한 writer, item 정보를 등록한 writer만 조회 가능
- offer writer = 자신이 등록한 제안만
- item writer = 해당 item의 모든 제안

1. item 조회

   - item writer&password 와 동일한지 > item writer 인 경우, 해당 item의 offer 전부 조회
2. offer 조회
 
   - offer writer&password 와 동일한지 > offer writer 인 경우, 작성한 offer 전부 조회
3. 그 외 > 조회 되는 값 없음

- `GET /items/{itemId}/offers?writer=jeeho.edu&password=qwerty1234&page=1&limit=5`
    
    Request Body: 없음
    
    Response Status: 200
    
    Response Body: 
    
    ```json
    {
        "content": [
    	      {
                "id": 1,
    				    "suggestedPrice": 1000000,
                "status": "거절"
            },
    	      {
                "id": 2,
    				    "suggestedPrice": 1200000,
                "status": "제안"
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
    }
    ```
    

### (offer writer) 제안 삭제

- 제안을 등록한 writer만 제안을 삭제할 수 있다.
- writer & password를 첨부해서 삭제

- `DELETE /items/{itemId}/offers/{proposalId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234"
    }
    ```
    
    Response Body: 
    
    ```json
    {
        "message": "제안을 삭제했습니다."
    }
    ```
    
    `writer` 와 `password` 가 제안 등록할 때의 값과 일치하지 않을 경우 실패
    

### (offer writer) 제안 수정 - suggestedPrice 수정

- 제안의 writer, password가 동일할 때만 수정 가능
- offer를 등록한 사용자만 suggestedPrice 변경 가능

- `PUT /items/{itemId}/offers/{proposalId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234",
        "suggestedPrice": 1100000
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "제안이 수정되었습니다."
    }
    ```
    
    `writer` 와 `password` 가 물품 등록할 때의 값과 일치하지 않을 경우 실패
    

### (item writer) 제안 수락 & 거절

- item을 등록할 때 writer & password 첨부
- item을 등록했던 사용자만 status 변경 가능
1. item 조회
    1. item의 writer, password와 일치하는지 확인
2. offer 조회
    1. offer의 itemId와 item의 Id가 일치하는지 확인
3. status 수정 > 저장

- `PUT /items/{itemId}/offers/{proposalId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.dev",
        "password": "1qaz2wsx",
        "status": "수락" || "거절"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "제안의 상태가 변경되었습니다."
    }
    ```
    
    `writer` 와 `password` 가 물품 등록할 때의 값과 일치하지 않을 경우 실패
    

### (offer writer) 제안 확정

- 제안이 수락 상태일 경우, offer의 status를 “확정”으로 변경이 가능하다
- writer & password를 첨부해서 요청
- offer writer만 구매 확정을 할 수 있음
- 구매 제안이 확정될 경우, 대상 물품의 상태는 **판매 완료**가 된다.
- 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 **거절**이 된다.\

1. offer 조회
    1. offer의 writer & password와 일치하는지 확인
    2. offer의 status가 “수락”인지 확인
2. “수락”인 경우, status를 “확정”으로 변경
3. 해당 itemId를 가진 나머지 offer의 status를 “거절”로 변경 
4. item 조회
    1. item의 status를 “판매 완료”로 변경
- `PUT /items/{itemId}/offers/{proposalId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234"
        "status": "확정"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "구매가 확정되었습니다."
    }
    ```
    
    `writer` 와 `password` 가 제안 등록할 때의 값과 일치하지 않을 경우 실패
    
    제안의 상태가 **수락**이 아닐 경우 실패
    

### 테스트

item 정보

![Untitled](doc/image/day3/img1.png)

해당 item에 등록된 offer 정보

![Untitled](doc/image/day3/img2.png)

1. 1번 offer의 status를 확정으로 변경
    - `PUT http://localhost:8080/items/1/offers/1`
    
    ```java
    {
        "writer": "yoo",
        "password": "1234",
        "status": "확정"
    }
    ```
    
2. 실행 결과

item 정보

![Untitled](doc/image/day3/img3.png)

해당 item에 등록된 offer 정보

![Untitled](doc/image/day3/img4.png)
