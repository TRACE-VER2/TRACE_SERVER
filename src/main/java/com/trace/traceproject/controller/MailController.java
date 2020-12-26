package com.trace.traceproject.controller;

import com.trace.traceproject.advice.exception.NoSuchEntityException;
import com.trace.traceproject.domain.Member;
import com.trace.traceproject.dto.response.model.SingleResult;
import com.trace.traceproject.util.MailUtil;
import com.trace.traceproject.service.MemberService;
import com.trace.traceproject.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final ResponseService responseService;

    //인증 이메일 전송
    @GetMapping("/verification")
    public SingleResult mailVerification(@RequestParam("mail") String mail) {
        Map<String, String> result = new HashMap<>();

        String verificationKey = mailUtil.sendValidationEmail(mail);
        result.put("verificationKey", verificationKey);

        return responseService.getSingleResult(result);
    }

    //비밀번호 찾기 이메일 전송
    @GetMapping("/password")
    public SingleResult findPassword(@RequestParam("userId") String userId, @RequestParam("name") String name,
                                       @RequestParam("phoneNum") String phoneNum) {
        Map<String, String> result = new HashMap<>();

        Member member = memberService.findByNameAndPhoneNum(name, phoneNum);
        if(!member.getUserId().equals(userId)){
            throw new NoSuchEntityException("유효하지 않은 회원 id입니다.");
        }

        //임시 비밀번호 발급
        String tmpPwd = mailUtil.sendPasswordChangeEmail(member.getEmail(), member.getName());
        
        //회원 비밀번호 변경
        memberService.changePassword(member.getId(), passwordEncoder.encode(tmpPwd));

        result.put("tmpPassword", tmpPwd);

        return responseService.getSingleResult(result);
    }
}
