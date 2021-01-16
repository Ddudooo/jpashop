package com.springjpa.study.jpashop.service;

import com.springjpa.study.jpashop.domain.Member;
import com.springjpa.study.jpashop.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;

    /**
     * 회원 가입.
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicatesMember(member);
        memberRepo.save(member);
        return member.getId();
    }

    private void validateDuplicatesMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepo.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepo.findAll();
    }

    //단일 회원 조회
    public Member findOne(Long id) {
        return memberRepo.findOne(id);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepo.findOne(id);
        member.setName(name);
    }
}