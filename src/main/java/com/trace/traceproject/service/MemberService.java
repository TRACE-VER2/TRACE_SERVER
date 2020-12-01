package com.trace.traceproject.service;

import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.enums.Tag;
import com.trace.traceproject.dto.MemberJoinDto;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PreferenceRepository preferenceRepository;

    //회원가입
    @Transactional
    public Long join(MemberJoinDto memberJoinDto) {
        validateDuplicateMember(memberJoinDto);

        Member member = memberJoinDto.toEntity();
        memberJoinDto.getPreferences().stream()
                .map(Tag::valueOf)
                .forEach(tag -> member.getPreferences().add(preferenceRepository.findByTag(tag)));
        return memberRepository.save(member).getId();
    }

    private void validateDuplicateMember(MemberJoinDto memberJoinDto) {
        if (memberRepository.findByNameAndPhoneNum(memberJoinDto.getName(), memberJoinDto.getPhoneNum()) != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
    }


}
