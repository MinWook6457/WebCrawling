### 백엔드 요구사항
|번호|기능|세부사항|
|:---:|:---:|:---:|
| R01 | 네이버 IT 뉴스 일반 크롤링 하여 DB에 저장 | DB 정보 : 제목, 내용(요약), 언론사명, 업로드 시기, 이미지  |
| R02 | 버튼 클릭 시 크롤링 진행|DB 초기화 및 재생성 필요|
| R03 | Spring Boot 사용 | Design Pattern: MVC |
| R04 | DB CRUD  | Java Persistence API (Hibernate) |
| R05 | 페이지네이션 시 DB에서 작업| limit , offset으로 잘라서 가져오기 |


### 프론트엔드 요구사항
|번호|기능|세부사항|
|:---:|:---:|:---:|
| R01 | 초기페이지 | 버튼, 표(제목, 내용 ... 등등) , 드랍 다운 메뉴(10,20,30개 씩 보기) |
| R02 | 버튼 클릭 시 <br> 크롤링 진행|크롤링 후 초기 페이지(1페이지)를 표시|
| R03 | 페이지 네이션| 첫 페이지 시 First , Prev 비활성화 <br> 페이지 그룹에서 Prev 비활성화 <br> First : 첫 페이지로 이동 <br> Prev : 이전 페이지로 이동 <br> 마지막 페이지 시 Next, End 비활성화 <br> 마지막 그룹에서 Next 비활성화 <br> End : 마지막 페이지로 이동 <br> Next : 다음 페이지로 이동|
| R04 |	페이지네이션 조건 | server-side로 구현하기(화면에 출력할 항목만 읽어오기) <br> 테이블 내용, 페이지네이션 요소는 JS로 그리기(테이블 동적 draw) <br> 통신은 ajax 이용한 비동기 통신(jquery 필요) <page unit(한 페이지 당 항목 수) select로 선택(ex. 10, 20, 30)> |

### 최종 화면
![image](https://github.com/MinWook6457/WebCrawling/assets/103114126/9a28ef3f-1bcf-44a9-a159-762be62038c8)

### 블로그 기록
[웹 크롤링 기록 - 1](https://minwook6457.tistory.com/21) <br>
[웹 크롤링 기록 - 2](https://minwook6457.tistory.com/24) <br>
[웹 크롤링 기록 - 마지막](https://minwook6457.tistory.com/25)

