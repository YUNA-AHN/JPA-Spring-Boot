# 02. 회원 서비스 개발

## 회원 서비스 코드
- @Service
- @Transactional: 트랜잭션, 영속성 컨텍스트
  - readOnly = true : 데이터 변경이 없는 읽기 전용 메서드에서 사용. 영속성 컨테스트를 플러시 하지 않으므로 약간의 성능향상(읽기전용에는 다 적용)
  - 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
  - 전체적으로 @Transactional(readOnly = true)하여도 부분적으로 @Transactional 호출하면 그것이 우선순위를 가진다!
- @Autowired : 생성자 injection 많이 사용. 생성자가 하나면 생략 가능
- 중복 체크: 실무에서는 멀티 스레드와 같은 상황을 고려하여 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전
```java


```
### 스프링 필드 주입 대신에 생성자 주입을 사용하자!  
- 생성자 인젝션 사용 -> 생성할 때 완성, 중간에 변경 불가능
- 생성자 주입 방식을 권장
- 변경 불가능한 안전한 객체 생성 가능
- 생성자가 하나라면, @Autowired 생략 가능
- final 키워드를 추가하면 컴파일 시점에 memberRepository를 설정하지 않는 오류 체크 가능(보통 기본 생성자 추가 시 발견)

필드 주입
```java
public class MemberService {
    @Autowired
    MemberRepository memberRepository;
    ...
}
```

생성자 주입
```java
public class MemberService {
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    ...
}
```

### lombok
```java
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    ...
}
```

+) 스프링 데이터 JPA를 사용하여 EntityManger도 주입 가능
```java
@Repository
@RequiredArgsConstructor
public class MemberService {
    private final EntityManager em;
    ...
}
```

## 추가 공부
- @Autowired : 인젝션? 필드 인젝션?
- @Transactional