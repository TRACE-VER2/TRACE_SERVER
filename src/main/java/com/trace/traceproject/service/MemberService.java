package com.trace.traceproject.service;

import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.enums.Role;
import com.trace.traceproject.dto.request.MemberUpdateDto;
import com.trace.traceproject.domain.enums.Tag;
import com.trace.traceproject.dto.request.MemberJoinDto;
import com.trace.traceproject.repository.MemberRepository;
import com.trace.traceproject.repository.PreferenceRepository;
import com.trace.traceproject.security.dto.UserInfo;
import com.trace.traceproject.security.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDbService {

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
        //최초가입시 USER권한 부여
        member.getRoles().add(Role.ROLE_USER);
        return memberRepository.save(member).getId();
    }

    private void validateDuplicateMember(MemberJoinDto memberJoinDto) {
        if (memberRepository.findByNameAndPhoneNum(memberJoinDto.getName(), memberJoinDto.getPhoneNum()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NoSuchEntityException("존재하지 않는 회원입니다."));
    }

    public Member findByNameAndPhoneNum(String name, String phoneNum) {
        return memberRepository.findByNameAndPhoneNum(name, phoneNum)
                .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 회원 정보입니다."));
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(()->new NoSuchEntityException("유효하지 않은 회원 id입니다."));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 이메일 주소입니다."));
    }

    //회원 정보 수정
    @Transactional
    public void update(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findByUserId(memberUpdateDto.getUserId())
                .orElseThrow(()->new NoSuchEntityException("유효하지 않은 회원 id입니다."));
        member.changeUserInfo(memberUpdateDto.getPhoneNum(),
                memberUpdateDto.getPreferences().stream()
                            .map(Tag::valueOf)
                            .map(preferenceRepository::findByTag)
                            .collect(Collectors.toSet()));
    }

    //비밀번호 수정
    @Transactional
    public void changePassword(Long id, String password) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 회원 id입니다."));

        member.changePassword(password);
    }

    @Transactional
    public void deleteByUserId(String userId) {
        memberRepository.deleteByUserId(userId);
    }

    //security context에 저장할 userInfo 불러오는 메서드
    @Override
    public UserInfo getUserInfo(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchEntityException("유효하지 않은 회원id입니다."));

        Set<String> roles = member.getRoles().stream().map(Enum::name).collect(Collectors.toSet());

        return UserInfo.builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .email(member.getEmail())
                .roles(roles)
                .build();
    }
}
