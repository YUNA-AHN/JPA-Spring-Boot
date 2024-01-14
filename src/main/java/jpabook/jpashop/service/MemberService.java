package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 컴포넌트라서 자동등록
@Transactional(readOnly = true) //JPA 모든 로직은 트랙젝션 내에서 실행되어야 한다
@RequiredArgsConstructor
public class MemberService {

//    @Autowired // 스프링이 스프링빈에 등록되어 멤버 리포지토리를 인젝션 해준다! 필드 인젝션?
    private final MemberRepository memberRepository;

    /***
     * 회원가입
     */
    // join으로 멤버 객체 넘기도록
    @Transactional // readOnly = false
    public Long join(Member member){
        // 중복회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    // EXCEPTION
    private void validateDuplicateMember(Member member) {
        // 이름 조회로 중복 확인
        List<Member> findMembers =  memberRepository.findByName(member.getName());
        // 비어있지 않다면 예외 처리
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}