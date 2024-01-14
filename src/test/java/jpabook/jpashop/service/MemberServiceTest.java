package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.respository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

// 완전히 스프링이랑 인티그레이션을 해서 테스트를 할 예정 : 아래 두 문장이 있어줘야지 할 수 잇음
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 데이터를 변경해야하기 때문 + 롤백
public class MemberServiceTest {

    // 테스트 케이스이니까 다른 애들이 참조할 일이 없으므로 간단하게!
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    // given이 주어졌을 때 when then 이렇게 된다.
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

        // when

        //
    }
}