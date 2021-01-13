package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.repo.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입")
    public void joinMember() throws Exception {
        //given
        Member member = new Member();
        member.setName("lee");
        //when
        Long saveId = memberService.join(member);
        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test()
    @DisplayName("중복 회원 검사")
    public void validationJoinMember() throws Exception {
        //given
        Member member = new Member();
        member.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");
        //when
        memberService.join(member);
        //then
        IllegalStateException thrown = assertThrows(IllegalStateException.class,
            () -> memberService.join(member2)
        );
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }
}