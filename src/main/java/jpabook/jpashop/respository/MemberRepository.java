package jpabook.jpashop.respository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    // 스프링이 이 엔티티 매니저를 만들어서 주입해준다!
//    @PersistenceContext
    private final EntityManager em;

    // JPA가 이것을 저장하는 로직
    public void save(Member member){
        em.persist(member);
    }
    // 단건 조회
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
