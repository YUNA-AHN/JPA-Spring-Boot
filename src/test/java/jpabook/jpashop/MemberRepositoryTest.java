package jpabook.jpashop;

//import static org.junit.Assert.*;
//import jpabook.jpashop.domain.Member;
//import jpabook.jpashop.MemberRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
//import javax.persistence.EntityManager;
//@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void testMember() {
        Member member = new Member();
        member.setUsername("memberA");
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장 : 같은 영속성 컨텍스트 안에서는 id 같은 엔티티로 인식한다
        System.out.println("findMember == member: " + (findMember == member));
    }
}