# 🏡트레이스 - 지역기반 자취방 후기 커뮤니티 플랫폼

![logo1](https://user-images.githubusercontent.com/40594564/102929876-c0065100-44de-11eb-9fc4-f5489f485df2.png)

## 🤔서비스 소개

트레이스란 “자취(自炊)의 자취(Trace)”라는 의미로, 웹, 앱 기반 자취방 커뮤니티 및 플랫폼 서비스입니다. 트레이스는 대학생을 주요 대상으로 하고 학교를 거점으로 하며, 학교 주변 학생들의 투명하고 높은 질의 주거에 도움을 주고자 합니다. 저희 서비스가 제공하는 기능은 크게 두가지로 나뉩니다.

1) 정보제공기능

    학생들이 집을 구할 때 직접 살아보지 않으면 알 수 없는 정보들(습기, 방음, 벌레 여부 등등) 과 건축물대장정보를 이용해 학교 주변 모든 건축물에 대한 기본 정보, 데이터분석을 이용한 적정 집값을 제공합니다. (필요한 경우 자취방 거래도 일부 보조합니다)

2) 커뮤니티 기능

    학생 사용자들이 자유롭게 이용할 수 있는 커뮤니티 서비스 역시 제공합니다. 각자 자취를 하면서 얻은 노하우를 공유하거나 자취할 때 필요한 물품에 대한 정보를 자유롭게 공유할 수 있습니다.

## 🙌🏻 트레이스 홈페이지

[트레이스 홈페이지 - 제작중 (클릭)](http://www.jaggutrace.com/)

---

## 🔥SERVER - 이수영👨🏻‍💻 배준호👨🏻‍🔧 배지홍👨🏻‍🏫

### 🤼 역할 분담

- 이수영 :
    - 회원가입, 로그인 기능 구현
    - JWT 기반 인증 인가 구현
    - ERD 작성 
    - API 설계
    - 기본 CRUD API 구현
    - AWS S3 버킷 연결
    - 배포 스크립트 작성 및 배포
- 배준호 : 
    - AWS EC2 배포 환경 구축
    - AWS RDS 인스턴스 생성
    - AWS Route53 이용 도메인 연결
    - DB에 건물 데이터 INSERT
- 배지홍 : 
    - 공공 API를 통해 얻은 건물 정보 가공 및 데이터 분석

### 🙆🏻‍♂️ 커밋 컨벤션

앞에 키워드를 적고 코드에 관한 설명 적기

**[ADD]**

- 기능 추가

**[DELETE]**

- 기능 삭제

**[MODIFY]**

- 기능 변경

**[BUGFIX]**

- 버그 수정

**[REFACTORING]**

- 코드 리팩토링
- 메인 코드에 직접적인 수정이 발생한 경우

**[FORMAT]**

- 코드형식 정렬 주석 등을 변경했을때
- 메인 코드 수정했으나 동작에 영향 주는 변화 없음

**[TEST]**

- 테스트코드 수정, 추가, 삭제시

**[DOC]**

- 문서 추가 삭제 변경(리드미 같은거)

**[PROJECT]**

- 프로젝트 관리 측면에서 변경한거(빌드스크립트 수정, git 설정, 배포 설정 등)

**[ETC]**

- 위에 해당되지 않는 모든 변경

Ex)

회원가입 기능 추가해서 올릴시(main 코드 부분)

[ADD] user join

회원 가입 기능 테스트 완료시(test 코드 부분)

[TEST] join test finished
    
---

## ✨기술 스택

![트레이스 기술스택](https://user-images.githubusercontent.com/40594564/102929972-efb55900-44de-11eb-8b9d-785660d707f2.png)

## 🏗️ 서버 아키택처

![서버 아키텍처](https://user-images.githubusercontent.com/40594564/102990179-4dd35200-455a-11eb-8f82-d07ea7c5f359.png)

## 📊 ERD

### 관계형 DB 테이블
![erd](https://user-images.githubusercontent.com/40594564/103280665-4c9b9c80-4a14-11eb-8959-8c0b334e9b7d.png)

### JPA 엔티티 클래스 
![entityManagerFactory(EntityManagerFactoryBuilder)](https://user-images.githubusercontent.com/40594564/103279971-89669400-4a12-11eb-8cbb-2c52a9a279e7.png)

## 🖨️API 명세서

### 스프레드 시트

![api 명세서](https://user-images.githubusercontent.com/40594564/104128822-19122800-53ad-11eb-843d-e19a21be80c3.png)

### API 명세서

- [git book api 링크](http://syleemk.gitbook.io/trace/)