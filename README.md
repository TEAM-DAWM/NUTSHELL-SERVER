<div align="center">

# 🌰 Nutshell 🌰
</div>
<p align="center">
  <img src="https://github.com/TEAM-DAWM/NUTSHELL-SERVER/assets/128598386/e97076fc-c226-41ac-a989-0a74b602defc"
" width=750px>
</p>
<div align="center">
  
        한 눈에 보는 내 삶의 대시보드

</div>


&nbsp;

## 🌈 NUTSHELL Server Developers
<!DOCTYPE html>


<table>
    <tr>
        <th>조민우</th>
        <th>조영주</th>
    </tr>
    <tr>
        <td><img width="300px"src="https://github.com/TEAM-DAWM/NUTSHELL-SERVER/assets/128598386/f9f469c9-6009-4e04-b211-8c88c02ed5b9"
></td>
        <td><img width="300px" src="https://github.com/TEAM-DAWM/NUTSHELL-SERVER/assets/128598386/077d35e0-e918-4315-9471-5c4627d5b433"
></td>
    </tr>
    <tr>
        <td>
            Entity 초기 세팅<br>
            CI/CD 구축<br>
            ERD 및 DB 설계<br>
            Google Calendar 연동
        </td>
        <td>
            AWS 서버 구축<br>
            CI/CD 구축<br>
            ERD 및 DB 설계<br>
            Google Login 연동
        </td>
    </tr>
    <tr>
        <td><a href="https://github.com/minwoo0419">@Minwoo Cho</a></td>
        <td><a href="https://github.com/choyeongju">@becahu</a></td>
    </tr>
</table>

</body>
</html>



&nbsp;


## 🍑 API docs
[Nutshell API 명세서 보고싶어요 ❗](https://topaz-work-262.notion.site/NutShell-API-909b69f8b9f348bc9bc6e76453ee4eb1?pvs=4)

&nbsp;


## 🍋 Code Convention
[Nutshell 코드 컨벤션 보고싶어요 ❗](https://www.notion.so/spring-code-convention-84696b53b3d04759a4d07a5257e2b729?pvs=21)


&nbsp;


## 💌 Commit Convention
```
[<Prefix>] #<Issue_Number> <Description>
```

```
[feat]: 새로운 기능 구현 
[fix]: 수정 
[refac]: 내부 로직은 변경하지 않고 기존의 코드를 개선하는 리팩토링 
[chore]: 패키지 구조 수정, 의존성 추가, yml 수정, 파일 이동 등 작업
[docs]: 문서 추가, 수정, 삭제 
[init]: 프로젝트 초기 세팅 
[test]: 테스트 코드 작성, 수정 
[hotfix] : hotfix
```

&nbsp;


## ✨Tech Stack
| 사용기술 | 정보 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.3.1, Gradle |
| Authentication | Spring Security, JSON Web Tokens |
| Database | AWS RDS(MySQL) |
| Instance | AWS EC2(Ubuntu) |
| CI/CD | Github Actions, Docker, Nginx |
| ERD | ERD Cloud, DataGrip |
| API Docs | Notion, Swagger |
| Redis | 7.2.5 |
| Orm | Spring Data JPA |

&nbsp;

## 💻 System Architecture
![image](https://github.com/TEAM-DAWM/NUTSHELL-SERVER/assets/128598386/84323db3-52f2-474f-bf5b-b3f3d6346c6e)


&nbsp;


## 🌱 ERD
<img width="1385" alt="image" src="https://github.com/TEAM-DAWM/NUTSHELL-SERVER/assets/103352114/0a728c4e-4437-4872-b5eb-7c815186eb5f">

&nbsp;

## 📂 Directory

```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┣ 📂nutshell
 ┃ ┃ ┃ ┣ 📂server
 ┃ ┃ ┃ ┃ ┣ 📂advice
 ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┣ 📂constant
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┗ 📂type
 ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┗ 📂code
 ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┗ 📂info
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┣ 📂swagger
 ┃ ┃ ┃ ┃ ┗ 📂utils
 ┃ ┗ 📂resources
```
