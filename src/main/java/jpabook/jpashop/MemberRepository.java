package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

// 스프링부트를 사용하기 때문에 스프링 컨테이너 위에서 모든게 작동!
// PersistenceContext annotation 있으면 엔티티 매니저 주입해준다
@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    // 저장
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    // 조회
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
