# 04. 엔티티 설계시 주의점
1. 엔티티에는 가급적 Setter를 사용하지 말것
   - 유지보수가 어렵기 때문! 추후에 리팩토링으로 Setter 제거

2. **모든 연관관계는 지연로딩으로 설정**
   - 즉시로딩(EAGER) 예측이 어려움, 어떤 SQL이 실행될지 추적하기 어려움! 연관된 데이터를 다 긁어오기 때문.. 특히 JPQL을 싱핼항 떄 N+1 문제 발생
   - 실무에서 모든 연관관계는 지연로딩(LAZY)로 !!!
   - 연관관 엔티티를 함께 DB에서 조회해야 한다면, fetch join 또는 엔티티 그래프 기능 사용
   - @XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정해야함

3. 컬렉션은 필드에서 초기화하자
   - 컬렉션은 필드에서 바로 초기화하는 것이 안전
   - null 문제에서 안전
   - 하이버네이트는 엔티티를 영속화(persist)할 때,컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경! => 하이버네이트가 추적할 수 있도록 변경
   만약 getOrders()처럼 임의의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있음
   따라서 필드레벨에서 생상하는 것이 가장 안전. 코드도 간결
    ```java
    Member member = new Member();
    System.out.println(member.getOrders().getClass());
    em.persist(member);
    System.out.println(member.getOrders().getClass());
    
    //출력 결과
    class java.util.ArrayList
    class org.hibernate.collection.internal.PersistentBag
    ```

4. 테이블, 컬럼명 생성 전략
- 하이버네이트 기존 구현: 엔티티의 필드명을 그대로 테이블의 컬럼명으로 사용(SpringPhysicalNamingStrategy)
- 스프링 부트 신규 설정 (엔티티(필드) => 테이블(컬럼))
  1. 카멜케이스 -> 언더스코어(memberPoint => member_point)
  2. .(점) => _(언더스코어)
  3. 대문자 => 소문자
  
- 적용 2단계 : 다른 전략으로 이름 생성시
  1. 논리명 생성: `명시적으로 컬럼, 테이블명을 직접 적지 않으면` ImplicitNamingStrategy 사용
     `spring.jpa.hibernate.naming.implicit-strategy` : 테이블이나, 컬럼명을 명시하지 않을 때 논리명 적
     용,
  2. 물리명 적용:
     `spring.jpa.hibernate.naming.physical-strategy` : `모든 논리명에 적용됨`, 실제 테이블에 적용
     (username usernm 등으로 회사 룰로 바꿀 수 있음)

  **스프링 부트 기본 설정**  
  `spring.jpa.hibernate.naming.implicit-strategy:
  org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy`
  `spring.jpa.hibernate.naming.physical-strategy:
  org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy`


## cascade
cascade는 persist를 전파

## 연관관계 메서드
양방향 로직을 짜줘야할 것 아니냐..?!, 핵심적으로 컨트롤하는 쪽이 가지고 있는 것이 좋다.
order-member / order-orderitem / order-delivery /category-child
연관관계 편의 메서드 양방향 연결하는 것을 원자적으로 한 코드로 해결

## 추후 공부할 것
- 지연로딩과 즉시로딩
- fetch join // 엔티티 그래프 기능