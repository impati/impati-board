# impati-board

impati 서비스의 오류 ,건의 , 피드백 게시판를 관리하는  API 프로젝트


# API 문서

- [게시판 API](https://service-hub.org/board/docs/articles.html)
- [게시판 댓글 API](https://service-hub.org/board/docs/comment.html)
- [에러 API](https://service-hub.org/board/docs/error.html)



# application.yml

- src/main/resources

```yaml
spring:
  profiles:
    active: ${spring.profiles.active}
  datasource:
    url: jdbc:mysql://${IP}/${database}
    username: ${USER}
    password: ${PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        format_sql: false
        default_batch_fetch_size: 1000
        show_sql: false
    database: mysql
    open-in-view: false


customer:
  clientId: ${clientId}
  server: https://impati-customer.com

logging:
  config: classpath:logback-spring-${spring.profiles.active}.xml
```


- src/test/resources

```yml
spring:
  datasource:
    driver-class-name: org.h2.Driver 
    url: jdbc:h2:mem:testdb 
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    properties:
      hibernate:
        ddl-auto: create
    generate-ddl: true
    defer-datasource-initialization: false
  sql:
    init:
      mode: never

customer:
  clientId: ${clientId}
  server: https://impati-customer.com
```
