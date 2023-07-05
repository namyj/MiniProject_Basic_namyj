# 7/3 - 2일차 중고 물품 댓글 관리

<aside>
💡 DAY 2️⃣ **중고 물품 댓글 요구사항 `7/3`**

1. 등록된 물품에 대한 질문을 위하여 댓글을 등록할 수 있다. 
    1. 이때 반드시 포함되어야 하는 내용은 **대상 물품, 댓글 내용, 작성자**이다.
    2. 또한 댓글을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
2. 등록된 댓글은 누구든지 열람할 수 있다. 
    1. 페이지 단위 조회가 가능하다.
3. 등록된 댓글은 수정이 가능하다. 
    1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
4. 등록된 댓글은 삭제가 가능하다. 
    1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
5. 댓글에는 초기에 비워져 있는 **답글** 항목이 존재한다. 
    1. 만약 댓글이 등록된 대상 물품을 등록한 사람일 경우, 물품을 등록할 때 사용한 비밀번호를 첨부할 경우 답글 항목을 수정할 수 있다.
    2. 답글은 댓글에 포함된 공개 정보이다.
</aside>

### Schema

![Untitled](doc/image/day2/comment_erd.png)

### 댓글 API 구현

- [x]  댓글 등록
- [x]  댓글 조회 - 전체 & 페이지 단위 조회
- [x]  댓글 수정
- [x]  댓글 삭제
- [x]  답글 기능

### 댓글 등록

- 필수 항목 : 대상 물품, 댓글 내용, 작성자, 비밀번호
- 추가) 해당 itemId가 존재하는지 확인 > 없으면 등록 실패

- `POST /items/{itemId}/comments`

Request Body:

```json
{
    "writer": "jeeho.edu",
    "password": "qwerty1234",
    "content": "할인 가능하신가요?"
}
```

Response Status: 200

Response Body:

```json
{
    "message": "댓글이 등록되었습니다."
}
```

### 댓글 조회 - 전체 & 페이지 단위 조회

- 해당 item에 등록된 댓글 전체 & 페이지 조회
- page, limit 값을 전달하지 않을 경우 > 전체 조회 결과 출력

- `GET /items/{itemId}/comments` / `GET /items/{itemId}/comments?page=0&limit=10`

Request Body: 없음

Response Status: 200

Response Body: 

```json
{
    "content": [
	      {
            "id": 1,
				    "content": "할인 가능하신가요?",
            "reply": "아니요"
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

### 댓글 조회 - 단일

- commentId에 해당하는 댓글 조회

- `GET /items/{itemId}/comments/{commentId}`
    
    Request Body:
    
    ```json
    {
        "writer": "user 1",
        "password": "password1234",
        "content": "변경한 댓글"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "id": 1,
        "itemId": 10,
        "writer": "user 1",
        "password": "password1234",
        "content": "변경한 댓글",
        "reply": null
    }
    ```
    

### 댓글 수정

- 작성자 & 비밀번호 첨부해서 댓글 수정
- 작성자 & 비밀번호가 일치하지 않을 경우 > PasswordNotCorrectException 발생

- `PUT /items/{itemId}/comments/{commentId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234",
        "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "댓글이 수정되었습니다."
    }
    ```
    

### 댓글 삭제

- 작성자 & 비밀번호 첨부해서 댓글 수정
- 작성자 & 비밀번호가 일치하지 않을 경우 > PasswordNotCorrectException 발생

- `DELETE /items/{itemId}/comments/{commentId}`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.edu",
        "password": "qwerty1234"
    }
    ```
    
    Response Body: 
    
    ```java
    {
        "message": "댓글을 삭제했습니다."
    }
    ```
    

### 답글 기능

- 만약 댓글이 등록된 대상 물품을 등록한 사람일 경우, 물품을 등록할 때 사용한 비밀번호를 첨부할 경우 답글 항목을 수정할 수 있다.
- 물품 정보를 등록한 사람은 해당 물품에 달린 댓글에 답글을 작성할 수 있음
- item 정보의 작성자 & 비밀번호와 일치하지 않을 경우 > PasswordNotCorrectException 발생
1. item 정보 조회
2. comment 정보 조회
3. itemId 와 commetId가 동일한지
4. item의 writer, password 와 현재 요청의 writer, password 와 동일한지 확인

- `PUT /items/{itemId}/comments/{commentId}/reply`
    
    Request Body:
    
    ```json
    {
        "writer": "jeeho.dev",
        "password": "1qaz2wsx",
        "reply": "안됩니다"
    }
    ```
    
    Response Body:
    
    ```json
    {
        "message": "댓글에 답변이 추가되었습니다."
    }
    ```
    

---

### 테스트

1. 아이템 등록
    
    `PUT http://localhost:8080/items`
    
    ```java
    {
        "title": "중고 맥북 팝니다",
        "description": "2019년 맥북 프로 13인치 모델입니다",
        "minPriceWanted": 1000000,
        "writer": "nam",
        "password": "1234"
    }
    ```
    
    ```java
    {
        "title": "중고 책 팝니다",
        "description": "2018년 출판",
        "minPriceWanted": 20000,
        "writer": "nam",
        "password": "1234"
    }
    ```
    
2. 댓글 등록
    
    `PUT http://localhost:8080/items/1/comments`
    
    ```java
    {
        "writer": "yoo",
        "password": "1234",
        "content": "할인 가능하신가요?"
    }
    ```
    
    ```java
    {
        "writer": "ji",
        "password": "1234",
        "content": "구매 가능한가요?"
    }
    ```
    
3. 답글 수정
    
    `PUT http://localhost:8080/items/1/comments/1/reply`
    
    ```java
    {
        "writer": "nam",
        "password": "1234",
        "reply": "할인은 어려울 것 같습니다."
    }
    ```
    

    item 정보
    
    ![Untitled](doc/image/day2/img1.png)
    
    comment 정보
    
    ![Untitled](doc/image/day2/img2.png)

4. 수정된 답글 확인
    
    ![Untitled](doc/image/day2/img3.png)
