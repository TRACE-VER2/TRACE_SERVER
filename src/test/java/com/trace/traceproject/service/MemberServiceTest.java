package com.trace.traceproject.service;

import com.trace.traceproject.domain.Member;
import com.trace.traceproject.domain.Preference;
import com.trace.traceproject.domain.enums.Tag;
import com.trace.traceproject.dto.MemberJoinDto;
import com.trace.traceproject.dto.MemberUpdateDto;
import com.trace.traceproject.repository.PreferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private PreferenceRepository preferenceRepository;


    @BeforeEach
    public void before() {
        Preference preference1 = new Preference(Tag.CHEAP);
        Preference preference2 = new Preference(Tag.LARGE);
        Preference preference3 = new Preference(Tag.NO_BUG);
        Preference preference4 = new Preference(Tag.SUNNY);
        Preference preference5 = new Preference(Tag.QUIET);

        preferenceRepository.save(preference1);
        preferenceRepository.save(preference2);
        preferenceRepository.save(preference3);
        preferenceRepository.save(preference4);
        preferenceRepository.save(preference5);
    }

    @Test
    public void 회원가입() throws Exception {
        //given
        MemberJoinDto memberJoinDto = new MemberJoinDto("syleemk", "1234", "syleemk@naver.com", "이수영", "01012341234");
        List<String> preferences = memberJoinDto.getPreferences();
        preferences.add("CHEAP");
        preferences.add("SUNNY");

        //when
        Long join = memberService.join(memberJoinDto);
        Member findMember = memberService.findById(join);

        //then
        assertThat(findMember.getUserId()).isEqualTo(memberJoinDto.getUserId());
        assertThat(findMember.getEmail()).isEqualTo(memberJoinDto.getEmail());
        assertThat(findMember.getName()).isEqualTo(memberJoinDto.getName());
        assertThat(findMember.getPhoneNum()).isEqualTo(memberJoinDto.getPhoneNum());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        MemberJoinDto memberJoinDto1 = new MemberJoinDto("syleemk", "1234", "syleemk@naver.com", "이수영", "01012341234");
        MemberJoinDto memberJoinDto2 = new MemberJoinDto("kim", "1234", "kim@naver.com", "이수영", "01012341234");

        //when
        memberService.join(memberJoinDto1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(memberJoinDto2));
    }

    @Test
    public void 회원정보수정() throws Exception {
        //given
        MemberJoinDto memberJoinDto = new MemberJoinDto("syleemk", "1234", "syleemk@naver.com", "이수영", "01012341234");
        List<String> preferences = memberJoinDto.getPreferences();
        preferences.add("CHEAP");
        preferences.add("SUNNY");
        memberService.join(memberJoinDto);

        //when
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("syleemk","01034564567");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("QUIET");
        arrayList.add("LARGE");
        memberUpdateDto.setPreferences(arrayList);
        memberService.update(memberUpdateDto);

        //then
        Member findMember = memberService.findByUserId(memberUpdateDto.getUserId());
        assertThat(findMember.getPhoneNum()).isEqualTo(memberUpdateDto.getPhoneNum());
        findMember.getPreferences().forEach(System.out::println);
    }
}