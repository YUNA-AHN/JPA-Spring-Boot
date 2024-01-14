# 01. 회원 리포지토리 개발
핵심 도메인 비즈니스 개발

## 회원 리포지토리 코드
- @Repository : 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 변환
  - 컴포넌트 스캔의 대상, 스프링 부트 기본적인 동작 방식 : 하위 컴포트 스캔해 등록! => 자동 등록
- @PersistenceContext : 엔티티 매니저(EntityManger) 주입
- @PersistenceUnit: 엔티티 매니저 팩토리((EntityMangerFactory) 주입
```java
package jpabook.jpashop.respository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public class MemberRepository {
    // 스프링이 이 엔티티 매니저를 만들어서 주입해준다!
    @PersistenceContext
    private EntityManager em;

    // JPA가 이것을 저장하는 로직
    // persist하면 영속성 컨텍스트 member에 넣고 트랜잭션 커밋하는 시점에 DB에 반영
    public void save(Member member){
        em.persist(member);
    }
    
    // 단건 조회
    // findOne : jpa의 find(타입, PK) 사용
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 리스트 조회
    public List<Member> findAll() {
        // (JPQL, 반환타입),  ctrl+alt+n : 코드 합차기
        // JPQL : SQL과 거의 동일 / sql은 테이블 대상, JPQL은 엔티티 객체를 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 이름으로 회원 검색
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // 파라미터 바인딩
                .getResultList();
    }
}
```