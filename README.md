# [MiniProject] Mutsa Market
멋쟁이사자처럼 백엔드 스쿨 5기 미니 프로젝트
<br>

## 프로젝트 개요

- 1차 기간: 6/29 ~ 7/5
- 2차 기간: 7/26 ~
  

### ♻️멋사마켓♻️

사용자가 중고 물품을 자유롭게 올리고, 댓글로 소통하며, 구매 제안에 대한 수락이 가능한 중고 거래 플랫폼의 백엔드를 구현했다. 

### ERD

![Untitled](doc/image/readme.png)

### 주요 기능

1. 중고 물품 정보 API
    - 등록
    - 조회 - 전체 & 페이지 단위 조회
    - 조회 - 단일 조회
    - 수정
    - 이미지 수정
    - 삭제
2. 댓글 API 구현
    - 등록
    - 조회 - 전체 & 페이지 단위 조회
    - 수정
    - 삭제
    - 기능
3. 구매 제안 API
    - 등록
    - 조회 - offer writer & item writer
    - 수정
        - (offer writer) suggestedPrice 수정
        - (offer writer) 구매 확정
        - (item writer) 제안 수락&거절

## 일자 별 요구사항 구현

[6/29 - 1일차 중고 물품 관리](doc/md/day1.md)

[7/3 - 2일차 중고 물품 댓글 관리](doc/md/day2.md)

[7/5 - 3일차 **구매 제안 관리**](doc/md/day3.md)
