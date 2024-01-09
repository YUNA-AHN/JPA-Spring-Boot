# JPA와 DB 설정, 동작확인
- `main/resource/application.properties`삭제
- `main/resource/application.yml`생성
  - properties와 yml 둘 중 하나 선택, 설정 파일이 많아지고 복잡할 수록 yml이 더 낫다!

`main/resource/application.yml`
```yml
spring:
datasource: # 데이터베이스 관련 설정
url: jdbc:h2:tcp://localhost/~/jpashop
username: sa
password:
driver-class-name: org.h2.Driver

jpa: # JPA 관련 설정
hibernate:
ddl-auto: create # 자동으로 탭을 생성
properties:
hinernate:
#        show_sql: true # system out으로 나타남
        format_sql: true
looging.leve: # 로깅 레벨 설정
org.hibernate.SQL: debug # hibernate SQL를 debug 모드로 쓴다는 것
# org.hibernate.tyoe: trace
```

### Member 엔티티 생성
`jpabook.jpashop/Member` 생성

```java
package jpabook.jpashop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {
    
    @Id @GeneratedValue
    private Long id;
    private  String username;
}
```

레파지토리 : 엔티티를 찾아주는 역할

중략... 

## 쿼리 파라미터 로그 남기기
