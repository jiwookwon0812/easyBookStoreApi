# 🏪 EasyBookService - 상점 예약 시스템

> 상점을 등록/예약 및 이용할 수 있는 백엔드 API 입니다.

## 📚 Table of Contents
- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [ERD 다이어그램](#-erd-다이어그램)
- [설치 및 설정](#-설치-및-설정)
- [API 문서](#-api-문서)


## 📝 프로젝트 소개
EasyBookService는 사용자들이 상점을 등록하고 예약할 수 있는 기능을 제공하는 백엔드 API입니다. JWT를 이용한 인증 및 권한 부여를 통해 보안을 강화하였습니다.


## 💻 기술 스택
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.3.1
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL
- **보안**: Spring Security, JWT (JSON Web Token)
- **API 문서화**: Springdoc OpenAPI, Swagger
- **로그 관리**: Lombok, SLF4J

## ✨ 주요 기능
- **회원가입 및 로그인**: 회원 정보를 입력해 가입하고 로그인할 수 있습니다.
  - 회원가입 시 사용자의 이름, 이메일, 비밀번호, 권한을 입력합니다.
  - 로그인 시 이메일과 비밀번호로 인증을 진행합니다.
  - 로그인 후 JWT 토큰을 발급받아 인증된 요청을 수행할 수 있습니다.

- **상점 등록 및 삭제**: 상점을 등록하고 관리할 수 있습니다.
  - 관리자 권한이 있는 사용자가 상점 정보를 입력하여 등록/삭제 할 수 있습니다.
  - 상점 이름, 위치, 설명을 입력합니다.

- **상점 조회**: 등록된 상점을 조회할 수 있습니다.
  - 사용자는 상점 이름이나 위치를 기준으로 등록된 상점을 검색할 수 있습니다.
  - 검색 결과가 없을 경우, 적절한 예외 처리를 통해 사용자에게 알립니다.

- **상점 예약/취소**: 상점을 예약/취소 할 수 있습니다.
  - 사용자는 예약할 상점 이름, 위치, 예약자 이름, 이메일, 예약 날짜 및 시간을 입력하여 예약합니다.
  - 동일한 시간대에 중복 예약이 불가능하도록 확인합니다.

- **예약 조회**: 자신의 예약을 조회할 수 있습니다. + 점장은 자신의 상점의 예약 조회 가능
  - 사용자는 자신의 이름과 로그인한 이메일로 예약 정보를 조회할 수 있습니다.
  - 사용자는 자신의 예약 목록을 확인하고, 예약 번호 및 상태를 확인할 수 있습니다.
  - 점장은 상점의 예약을 날짜별로 조회 할 수 있습니다.

- **예약 승인 및 거절**: 상점 관리자가 예약을 승인하거나 거절할 수 있습니다.
  - 관리자 권한이 있는 사용자는 예약 번호를 기준으로 예약을 승인하거나 거절할 수 있습니다.
  - 승인된 예약만이 방문 확인 절차를 진행할 수 있습니다.

- **방문 확인**: 상점 예약 후 방문을 확인할 수 있습니다.
  - 사용자는 예약 번호와 예약자 이름을 입력하여 방문을 확인할 수 있습니다.
  - 10분 전까지 방문 확인이 가능하며, 그 이후에는 관리자에게 문의하도록 예외 처리 하였습니다.
  - 방문 확인 후, 관리자 권한이 있는 사용자에 의해 해당 예약의 상태가 '방문 완료'로 변경됩니다.
 
- **리뷰 작성/삭제**: 상점 예약 및 방문 후 리뷰를 작성/삭제 할 수 있습니다.
  - 사용자는 예약 번호와 예약자 이름, 상점 이름과 함께 상세 리뷰와 별점을 남길 수 있습니다.
  - 방문 확인이 된 예약건에 한해서 리뷰 작성이 가능합니다.
  - 방문 여부는 예약 번호와 예약자 이름을 통해 확인되며, 리뷰 저장시에는 로그인한 이메일이 본인 인증을 위해 함께 저장됩니다.

- **리뷰 조회**: 상점의 리뷰를 조회할 수 있습니다.
  - 특정 상점 이름을 요청 파라미터로 추가하여 해당 상점만의 리뷰들을 조회할 수 있습니다.
  - 사용자 본인이 작성한 리뷰들을 전부 조회할 수 있습니다.


## 📊 ERD 다이어그램
<img width="909" alt="image" src="https://github.com/user-attachments/assets/beda7b42-07f9-4f67-bc09-9fe8ee6e195f">



## 🔧 설치 및 설정
### MySQL 설정
1. MySQL을 설치하고 데이터베이스를 생성합니다.
2. `src/main/resources/application.properties` 파일에 데이터베이스 설정을 추가합니다:

   ```properties
   # MySQL
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.url=jdbc:mysql://localhost:3306/easybook?serverTimezone=UTC&characterEncoding=UTF-8
   spring.datasource.username=root
   spring.datasource.password=your_password

   # JPA
   spring.jpa.show-sql=true
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database=mysql

### JWT 설정
1. `src/main/resources/application.properties` 파일에 JWT 설정을 추가합니다:

    ```properties
    # JWT
    jwt.secret=your_secret_key

### Swagger 설정
1. `src/main/resources/application.properties` 파일에 Swagger 설정을 추가합니다:

    ```properties
    # Swagger by Springdoc
    springdoc.api-docs.path=/v3/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.api-docs.groups.enabled=true
    springdoc.swagger-ui.operations-sorter=alpha
    springdoc.swagger-ui.tags-sorter=alpha
    springdoc.swagger-ui.display-request-duration=true
    springdoc.swagger-ui.doc-expansion=none
    springdoc.swagger-ui.default-model-rendering=model

### Gradle 설정
1. `build.gradle` 파일을 다음과 같이 설정합니다:

   ```properties
   plugins {
       id 'java'
       id 'org.springframework.boot' version '3.3.1'
       id 'io.spring.dependency-management' version '1.1.5'
   }

   group = 'zerobase'
   version = '0.0.1-SNAPSHOT'

   java {
      toolchain {
          languageVersion = JavaLanguageVersion.of(17)
     }
   }

   configurations {
      compileOnly {
          extendsFrom annotationProcessor
        }
   }

   repositories {
       mavenCentral()
   }

   dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        implementation 'org.springframework.boot:spring-boot-starter-security'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        implementation 'io.jsonwebtoken:jjwt-impl:0.11.2'
        implementation 'io.jsonwebtoken:jjwt-jackson:0.11.2'
        implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4'
  
        compileOnly 'org.projectlombok:lombok'
        runtimeOnly 'com.mysql:mysql-connector-j'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation 'org.springframework.security:spring-security-test'
        testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
   }

   tasks.named('test') {
       useJUnitPlatform()
   }


## 📄 API 문서
API 문서는 Swagger를 통해 자동 생성됩니다.

서버를 실행한 후, 다음 주소에서 API 문서를 확인할 수 있습니다:

http://localhost:8080/swagger-ui/index.html
