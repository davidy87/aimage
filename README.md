# AImage
AI 이미지 생성 web application 

## Summery
<hr>
사용자가 prompt를 제공하면 그 설명을 바탕으로 AI가 그림을 생성해 주는 웹사이트입니다. OpenAI에서 제공하는 그림 생성 모델 DALL·E를 기반으로 한 API를 사용하였습니다. <br>

이미 Django framework를 사용하여 개발한 프로젝트이지만 Spring의 학습을 위하여 다시 Spring으로 개발하였습니다.


## Tech. Stack(s)
<hr>

* **Front-end**: Bootstrap
* **Back-end**: Spring (Spring Boot), Thymeleaf
* **Database**: SQLite3


## 이슈
### 예외 처리
<hr>
서버 안에서 일반적으로 일어날 수 있는 오류 (404, 500, ...) 들은 지정해둔 오류 페이지를 보여 주는 것으로 해결하였다. <br>

그런데 OpenAI로 이미지 생성을 테스트하다 무료 크레딧이 만료되면 `OpenAiHttpException` 을 반환한다는 것을 확인하였다.
* 이 예외는 기존 예외 처리와는 다른 방법으로 처리해야 한다고 생각했다.
* 계속 고민하다 좋은 방법이 나오기 전까지는 임시적으로 `try ~ catch` 문을 사용하고, 예외를 잡아내면 시작 페이지로 보내도록 처리하였다.
