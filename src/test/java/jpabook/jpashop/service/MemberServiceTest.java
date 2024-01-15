package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.respository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// 완전히 스프링이랑 인티그레이션을 해서 테스트를 할 예정 : 아래 두 문장이 있어줘야지 할 수 있움
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {
    // 테스트 케이스이니까 다른 애들이 참조할 일이 없으므로 간단하게!
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    // 값(given)이 주어졌을 때(when)이렇게 된다.(then)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("Kim");

        // when
        Long saveID = memberService.join(member);

        // then
        // 가입한 것과 찾아온 것이 같아야 정상적으로 회원가입!
        // 같은 트랜젝션 안에서 같은 엔티티 pk 값이 동일하다면 딱 하나로 관리
        assertEquals(member, memberRepository.findOne(saveID));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
//        memberService.join(member2); // 여기서 예외가 발생해야한다.
        // 이렇게 작성해주며 터지지 않고 테스트 가능, 더 간단한 방법 존재
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }

        // 중복 예외가 발생하는 부분
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then : 위에서 예외 발생 여부를 검증하므로 삭재 junit5라..
//        fail("예외가 발생해야 한다.");
    }

}